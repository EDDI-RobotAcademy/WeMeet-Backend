package com.example.demo.config;

import com.example.demo.security.costomUser.CustomUserDetailsService;
import com.example.demo.security.filter.JwtLoginFilter;
import com.example.demo.security.filter.TokenCheckFilter;
import com.example.demo.security.handler.JwtLoginFailHandler;
import com.example.demo.security.handler.JwtLoginSuccessHandler;
import com.example.demo.security.service.JwtService;
import com.example.demo.security.service.RedisService;
import com.example.demo.security.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    final CustomUserDetailsService customUserDetailsService;
    final JwtUtil jwtUtil;
    final RedisService redisService;
    final JwtService jwtService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.addFilterBefore(filter, CsrfFilter.class);

        http.cors();

        http.csrf().disable();

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        http.authenticationManager(authenticationManager);

        JwtLoginFilter jwtLoginFilter = new JwtLoginFilter("/user/sign-in");
        jwtLoginFilter.setAuthenticationManager(authenticationManager);
        jwtLoginFilter.setAuthenticationSuccessHandler(new JwtLoginSuccessHandler(jwtUtil, redisService, jwtService));
        jwtLoginFilter.setAuthenticationFailureHandler(new JwtLoginFailHandler());

        http.addFilterBefore(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class);

        TokenCheckFilter tokenCheckFilter = new TokenCheckFilter(jwtUtil, customUserDetailsService, redisService);
        http.addFilterBefore(tokenCheckFilter, UsernamePasswordAuthenticationFilter.class);

        return http
                .authorizeHttpRequests((authorizeRequests) -> {
                    authorizeRequests.requestMatchers("/user/sign-up", "/jwt/refresh", "/oauth", "/oauth/google-login", "/oauth/google","/user/check-nickname/**","/user/check-email/**"
                            ,"/oauth/kakao" ,"/oauth/kakao-login", "/moim/{id}",  "/moim/list", "/moim/{id}/joinable", "/payment", "/payment/hook", "/travel/airport/list", "/board/list/moim/**")
                            .permitAll();
                    authorizeRequests.requestMatchers("/user", "/moim", "/moim/{id}/user", "/travel/country/list", "/travel/city/list", "/travel/option/list", "/payment/moim/{moimId}", "/moim/list/**", "/board/moim/**", "/board/list/moim/**")
                            .hasAnyRole("NORMAL", "ADMIN");
                    authorizeRequests.requestMatchers("/jwt/refresh", "/user/sign-out")
                            .authenticated();
                    authorizeRequests.requestMatchers("/travel", "/travel/country/list")
                            .hasAnyRole("ADMIN");

                })
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
