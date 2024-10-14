package com.mh.userservice.service;

import com.mh.userservice.dto.UserReqDto;
import com.mh.userservice.dto.UserResDto;
import com.mh.userservice.entity.UserEntity;
import com.mh.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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

    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
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