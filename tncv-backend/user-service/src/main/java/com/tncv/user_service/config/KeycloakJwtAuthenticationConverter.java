package com.tncv.user_service.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakJwtAuthenticationConverter
        implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        Collection<GrantedAuthority> authorities =
                extractAuthorities(jwt);

        return new JwtAuthenticationToken(
                jwt,
                authorities,
                jwt.getSubject()
        );
    }

    private Collection<GrantedAuthority> extractAuthorities(
            Jwt jwt) {

        Map<String, Object> realmAccess =
                jwt.getClaim("realm_access");

        if (realmAccess == null) {
            return Collections.emptyList();
        }

        Object rolesObject =
                realmAccess.get("roles");

        if (!(rolesObject instanceof List<?> roles)) {
            return Collections.emptyList();
        }

        return roles.stream()
                .filter(role -> role instanceof String)
                .map(role ->
                        new SimpleGrantedAuthority(
                                "ROLE_" + role
                        )
                )
                .collect(Collectors.toList());
    }
}