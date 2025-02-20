package com.example.oauth2authorizationserver;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import java.util.List;
import java.util.UUID;

import static org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.CLIENT_CREDENTIALS;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.REFRESH_TOKEN;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.OPENID;

@Configuration
public class ClientsConfiguration {

    @Bean
    RegisteredClientRepository registeredClientRepository(JdbcTemplate template) {
        return new JdbcRegisteredClientRepository(template);
    }

    @Bean
    ApplicationRunner injectClientsRunner(RegisteredClientRepository registeredClientRepository) {
        return args -> {
            if (registeredClientRepository.findByClientId("spring-client") == null) {
                var springClient = RegisteredClient
                    .withId(UUID.randomUUID().toString())
                    .clientId("spring-client")
                    .clientSecret("{bcrypt}$2a$10$RDT8GcEeA0zfAkDBKD7G3OzcSarbD9XEa80b2.SbC9r9/NKqdVCni") // client secret is 'secret'
                    .clientAuthenticationMethod(CLIENT_SECRET_BASIC)
                    .authorizationGrantTypes(authorizationGrantTypes ->
                        authorizationGrantTypes.addAll(List.of(CLIENT_CREDENTIALS, AUTHORIZATION_CODE, REFRESH_TOKEN)))
                    .redirectUri("http://127.0.0.1:8082/login/oauth2/code/spring")
                    .scopes(scopes -> scopes.addAll(List.of(OPENID, "user.read", "user.write")))
                    .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                    .build();

                registeredClientRepository.save(springClient);
            }
        };
    }

}
