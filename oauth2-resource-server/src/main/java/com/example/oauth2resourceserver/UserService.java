package com.example.oauth2resourceserver;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    @PreAuthorize("hasAuthority('SCOPE_user.read')")
    public Map<String, Object> me() {
        var jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Map.of("name", jwt.getSubject());
    }

}


