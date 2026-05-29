package com.omar.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {

        return builder.routes()

                .route("user-service", r -> r
                        .path("/user/**")
                        .uri("lb://USER-SERVICE"))

                .route("quiz-service", r -> r
                        .path("/quiz/**")
                        .uri("lb://QUIZ-SERVICE"))

                .route("admin-quiz-service", r -> r
                        .path("/admin/quiz/**")
                        .uri("lb://QUIZ-SERVICE"))

                .route("admin-question-service", r -> r
                        .path("/admin/question/**")
                        .uri("lb://QUESTION-SERVICE"))

                .build();
    }
}