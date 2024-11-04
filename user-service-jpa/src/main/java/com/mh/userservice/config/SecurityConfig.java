package com.mh.userservice.config;

import com.mh.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    private final Environment environment;
    private final PasswordEncoder passwordEncoder;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // Configure AuthenticationManagerBuilder
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http.csrf((csrf) -> csrf.disable());
//        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests((authz) -> authz.requestMatchers("/**").permitAll()
//                                .requestMatchers(new AntPathRequestMatcher("/actuator/**")).permitAll()
//                                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
//                                .requestMatchers(new AntPathRequestMatcher("/users", "POST")).permitAll()
//                                .requestMatchers(new AntPathRequestMatcher("/welcome")).permitAll()
//                                .requestMatchers(new AntPathRequestMatcher("/health-check")).permitAll()
//                                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
//                                .requestMatchers(new AntPathRequestMatcher("/swagger-resources/**")).permitAll()
//                                .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
////                        .requestMatchers("/**").access(this::hasIpAddress)
//                                .requestMatchers("/**").access(
//                                        new WebExpressionAuthorizationManager("hasIpAddress('localhost') " +
//                                                "or hasIpAddress('127.0.0.1')")) // host pc ip address
//                                .anyRequest().authenticated()

//                        .requestMatchers("/**").access(this::hasIpAddress)
//                                .requestMatchers("/**").access(
//                                        new WebExpressionAuthorizationManager("hasIpAddress('127.0.0.1') or hasIpAddress('172.30.1.48')"))
//                                .anyRequest().authenticated()
//                                .anyRequest().permitAll()

                )
                .authenticationManager(authenticationManager)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilter(getAuthenticationFilter(authenticationManager));
        http.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.sameOrigin()));

        return http.build();
    }

    protected AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) {
        AuthenticationFilter authenticationFilter
                = new AuthenticationFilter(authenticationManager, userService, environment);
        authenticationFilter.setFilterProcessesUrl("/users/login");
        return authenticationFilter;
    }
}
