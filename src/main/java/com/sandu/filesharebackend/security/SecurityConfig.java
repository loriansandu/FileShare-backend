package com.sandu.filesharebackend.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            @Value("${security.allowed.origins}") final List<String> origins,
            @Value("${security.allowed.headers}") final List<String> headers,
            @Value("${security.allowed.methods}") final List<String> methods,
            @Value("${security.exposed.headers}") final List<String> exposedHeaders) throws Exception {

        httpSecurity.csrf().disable();

//        httpSecurity.authorizeHttpRequests().anyRequest().authenticated();


        httpSecurity.cors(cors -> {
            CorsConfigurationSource corsConfigurationSource = r -> {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowCredentials(true);
                corsConfiguration.setAllowedOrigins(origins);
                corsConfiguration.setAllowedHeaders(headers);
                corsConfiguration.setExposedHeaders(exposedHeaders);
                corsConfiguration.setAllowedMethods(methods);
                return corsConfiguration;
            };

            cors.configurationSource(corsConfigurationSource);
        });

        return httpSecurity.build();
    }
}
