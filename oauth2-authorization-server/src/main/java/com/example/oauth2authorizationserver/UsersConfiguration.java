package com.example.oauth2authorizationserver;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;

@Configuration
public class UsersConfiguration {

    @Bean
    JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    ApplicationRunner injectUsersRunner(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userDetailsManager.userExists("bernard")) {
                var user = User.withUsername("bernard")
                    .password(passwordEncoder.encode("pw"))
                    .roles("user")
                    .build();

                userDetailsManager.createUser(user);
            }
        };
    }

}
