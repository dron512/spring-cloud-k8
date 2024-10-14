package com.mh.userservice.service;

import com.mh.userservice.dto.UserDto;
import com.mh.userservice.dto.UserMapperDto;
import com.mh.userservice.repository.UserRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    Environment env;
    RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserMapperDto userMapperDto = userRepository.findByEmail(username);

        if (userMapperDto == null)
            throw new UsernameNotFoundException(username + ": not found");

        return new User(userMapperDto.getUser_email(),
                        userMapperDto.getUser_password(),
                        true,
                        true,
                        true,
                        true,
                        new ArrayList<>());
    }

    public UserDto createUser(UserDto userDto) {
        userDto.setUser_id(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserMapperDto userMapperDto = mapper.map(userDto, UserMapperDto.class);
        userMapperDto.setUser_password(passwordEncoder.encode(userDto.getUser_password()));

        userRepository.save(userMapperDto);

        UserDto returnUserDto = mapper.map(userMapperDto, UserDto.class);

        return returnUserDto;
    }

    public Iterable<UserMapperDto> getUserByAll() {
        return userRepository.findAll();
    }

    public UserDto getUserDetailsByEmail(String email) {
        UserMapperDto userMapperDto = userRepository.findByEmail(email);
        if (userMapperDto == null)
            throw new UsernameNotFoundException(email);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(userMapperDto, UserDto.class);
        return userDto;
    }
}