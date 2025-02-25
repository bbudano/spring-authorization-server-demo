package com.example.oauth2authorizationserver;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.springframework.security.oauth2.core.AuthorizationGrantType.*;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.OPENID;

@Component
public class DataInitRunner implements ApplicationRunner {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final RegisteredClientRepository registeredClientRepository;

    public DataInitRunner(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder, RegisteredClientRepository registeredClientRepository) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
        this.registeredClientRepository = registeredClientRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!userDetailsManager.userExists("bernard")) {
            var user = User.withUsername("bernard")
                    .password(passwordEncoder.encode("pw"))
                    .roles("user")
                    .build();

            userDetailsManager.createUser(user);
        }

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
    }
}
