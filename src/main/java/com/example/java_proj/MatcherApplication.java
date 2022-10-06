package com.example.java_proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import security.JWTAuthorizationFilter;

@SpringBootApplication
public class MatcherApplication {
    public static void main(String[] args) {
        SpringApplication.run(MatcherApplication.class, args);
    }

    @EnableWebSecurity
    @Configuration
    class WebSecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/").permitAll()
                    .antMatchers(HttpMethod.GET, "/completedOrders").permitAll()
                    .antMatchers(HttpMethod.GET, "/aggBuyOrders").permitAll()
                    .antMatchers(HttpMethod.GET, "/aggSellOrders").permitAll()
                    .antMatchers(HttpMethod.POST, "/addAccount").permitAll()
                    .antMatchers(HttpMethod.PUT, "/login").permitAll()
//                    .antMatchers(HttpMethod.POST, "/addOrder").permitAll()
                    .antMatchers( "/h2-console/**").permitAll()
                    .anyRequest().authenticated();
            http.headers(headers -> headers.frameOptions().sameOrigin());
            return http.build();
        }
    }
}
