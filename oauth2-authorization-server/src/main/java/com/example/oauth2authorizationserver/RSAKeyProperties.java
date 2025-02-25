package com.example.oauth2authorizationserver;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties("rsa")
public record RSAKeyProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
}
