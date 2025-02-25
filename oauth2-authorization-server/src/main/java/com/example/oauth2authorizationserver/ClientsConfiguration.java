package com.example.oauth2authorizationserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

@Configuration
public class ClientsConfiguration {

    // JdbcRegisteredClientRepository causes the following exception in DEBUG logs:
    // java.lang.ClassNotFoundException: org.springframework.security.cas.jackson2.CasJackson2Module
    // This is expected behavior, see issue:
    // https://github.com/spring-projects/spring-authorization-server/issues/1672

    @Bean
    RegisteredClientRepository registeredClientRepository(JdbcTemplate template) {
        return new JdbcRegisteredClientRepository(template);
    }

}
