package com.mh.userservice.service;

import com.mh.userservice.dto.UserReqDto;
import com.mh.userservice.dto.UserResDto;
import com.mh.userservice.entity.UserEntity;
import com.mh.userservice.feignclient.CatalogServiceClient;
import com.mh.userservice.feignclient.OrderServiceClient;
import com.mh.userservice.repository.UserRepository;
import com.mh.userservice.vo.ResponseCatalog;
import com.mh.userservice.vo.ResponseOrder;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final Environment env;
    private final RestTemplate restTemplate;

    private final OrderServiceClient orderServiceClient;
    private final CatalogServiceClient catalogServiceClient;

    private final CircuitBreakerFactory circuitBreakerFactory;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if (userEntity == null)
            throw new UsernameNotFoundException(username + ": not found");

        return new User(userEntity.getEmail(),
                        userEntity.getPassword(),
                        true,
                        true,
                        true,
                        true,
                        new ArrayList<>());
    }

    public UserResDto createUser(UserReqDto userReqDto) {
        userReqDto.setId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();

        UserEntity userMapperDto = mapper.map(userReqDto, UserEntity.class);
        userMapperDto.setPassword(passwordEncoder.encode(userReqDto.getPassword()));

        userRepository.save(userMapperDto);

        UserResDto returnUserDto = mapper.map(userMapperDto, UserResDto.class);

        return returnUserDto;
    }

    public List<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    public UserResDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null)
            throw new UsernameNotFoundException(userId);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        List<ResponseOrder> ordersList = new ArrayList<>();

        /* #1-1 Connect to order-service using a rest template */
        /* @LoadBalanced 로 선언헀으면, apigateway-service로 호출 못함 */
        /* http://ORDER-SERVICE/order-service/1234-45565-34343423432/orders */
//        String orderUrl = String.format(env.getProperty("order_service.url"), userId);
//        String orderUrl = String.format("http://127.0.0.1:8080/order-service/%s/orders", userId);
//        ResponseEntity<List<ResponseOrder>> orderListResponse =
//                restTemplate.exchange(orderUrl, HttpMethod.GET, null,
//                                            new ParameterizedTypeReference<List<ResponseOrder>>() {
//                });
//
//        ordersList = orderListResponse.getBody();

//        카타로그 서비스 테스트
//        List<ResponseCatalog> catalogList = new ArrayList<>();
//        String catalogUrl = "http://127.0.0.1:8080/catalog-service/catalogs";
//        ResponseEntity<List<ResponseCatalog>> catalogListResponse =
//                restTemplate.exchange(catalogUrl, HttpMethod.GET, null,
//                                            new ParameterizedTypeReference<List<ResponseCatalog>>() {
//                });
//        catalogList = catalogListResponse.getBody();
//        System.out.println(catalogList);

//        try {
//            ResponseEntity<List<ResponseOrder>> _ordersList = orderServiceClient.getOrders(userId);
//            ordersList = _ordersList.getBody();
//            ordersList = orderServiceClient.getOrders(userId);
//        } catch (FeignException ex) {
//            log.error(ex.getMessage());
//        }

        ordersList = circuitBreakerFactory.create("default")
                .run(
                     () -> orderServiceClient.getOrders(userId),
                    throwable -> new ArrayList()
                );

        // catalog 서비스 테스트
//        try {
//            ResponseEntity<List<ResponseOrder>> _ordersList = orderServiceClient.getOrders(userId);
//            ordersList = _ordersList.getBody();
//            List<ResponseCatalog> list = catalogServiceClient.getCatalogs();
//            log.info(list.toString());
//        } catch (FeignException ex) {
//            log.error(ex.getMessage());
//        }

        UserResDto userDto = mapper.map(userEntity, UserResDto.class);
        userDto.setOrders(ordersList);

        return userDto;
    }


    public UserResDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null)
            throw new UsernameNotFoundException(email);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserResDto userDto = mapper.map(userEntity, UserResDto.class);
        return userDto;
    }
}