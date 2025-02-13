package com.example.oauth2client;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {

    @Bean
    RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        String apiPrefix = "/api/";

        return routeLocatorBuilder
            .routes()
            .route(resourceServer -> resourceServer
                .path(apiPrefix + "**")
                .filters(f -> f
                    .tokenRelay()
                    .rewritePath(apiPrefix + "(?<segment>.*)", "/$\\{segment}"))
                .uri("http://localhost:8081"))
            .route(frontend -> frontend
                .path("/**")
                .uri("http://localhost:3000")
            )
            .build();
    }

}
