package com.sandu.filesharebackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class AppConfig {
//    @Bean
//    public CorsFilter corsFilter( @Value("${security.allowed.origins}") final List<String> origins,
//                                  @Value("${security.allowed.headers}") final List<String> headers,
//                                  @Value("${security.allowed.methods}") final List<String> methods,
//                                  @Value("${security.exposed.headers}") final List<String> exposedHeaders) {
//        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.setAllowedOrigins(origins);
//        corsConfiguration.setAllowedHeaders(headers);
//        corsConfiguration.setExposedHeaders(exposedHeaders);
//        corsConfiguration.setAllowedMethods(methods);
//        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
//        return new CorsFilter(urlBasedCorsConfigurationSource);
//    }
}
