package com.b2mark.common.security;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.b2mark.common.entity.security.jwt.JwtUser;
import com.b2mark.common.exceptions.ExceptionsDictionary;
import com.b2mark.common.exceptions.PublicException;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;

    public JwtTokenAuthenticationFilter(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // 1. get the authentication header. Tokens are supposed to be passed in the authentication header
        String header = request.getHeader(jwtConfig.getHeader());
        JwtUser jwtUser = null;
        // 2. validate the header and check the prefix
        if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
            chain.doFilter(request, response);        // If not valid, go to the next filter.
            return;
        }

        // All secured paths that needs a token are already defined and secured in config class.
        // And If user tried to access without access token, then he won't be authenticated and an exception will be thrown.
        // 3. Get the token
        String token = header.replace(jwtConfig.getPrefix(), "");

        try {    // exceptions might be thrown in creating the claims if for example the token is expired


            RSAKey rsaKey = RSAKey.parse("{\"kid\":\"HSJphMi7RYVfMUyeBGxjPHhh2715phDzqzdUWTl1TIg\",\"kty\":\"RSA\",\"alg\":\"RS256\",\"use\":\"sig\",\"n\":\"zv0loocOhwGhFaKXzkdHNwFYef-D3SFlJ1w8ZynT7NpA1Wg428Y1pqU4kHNjuBYSt3YoeGOKkvMJHiWvYbgfEjZltodGcAJYFRIoQ3-1XSi_WWo53DK_JtDEQUUsl9b0wrJdf7RX84ypBaU0AHUI94K0ts4P0ecPHwqqQuOZjHjBVPkLfHH6DSJ9n0v1DVnSpcFZcwBBPrcPl3hT90M_mbCWnu8-j4VU38FAHz66nRJVtwgpvGmo3sdia_Ue3bR9wEauKLOZ39mGUAP5RVncbUw6zslhCtrNuy0POSwx-u3Fi2UdH9hYzClx8UlBd5j6T8nvprRw_ulCKzefMpXEQw\",\"e\":\"AQAB\"}");


            PublicKey publicKey1 = rsaKey.toPublicKey();

            //validate the token.
            Claims claims = Jwts.parser().setSigningKey(publicKey1).parseClaimsJws(token).getBody();

            String username = claims.getSubject();
            if (username != null) {
                @SuppressWarnings("unchecked")
                LinkedHashMap<String, String> realm_access = (LinkedHashMap<String, String>) claims.get("realm_access");
                List<String> authorities = (ArrayList<String>) ((Object) realm_access.get("roles"));

                // 5. Create auth object
                // UsernamePasswordAuthenticationToken: A built-in object, used by spring to represent the current authenticated / being authenticated user.
                // It needs a list of authorities, which has type of GrantedAuthority interface, where SimpleGrantedAuthority is an implementation of that interface
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

                // 6. Authenticate the user
                // Now, user is authenticated
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception e) {
            // In case of failure. Make sure it's clear; so guarantee user won't be authenticated
            SecurityContextHolder.clearContext();
            throw new PublicException(ExceptionsDictionary.UNAUTHORIZED, e.getMessage());
        }

        // go to the next filter in the filter chain
        chain.doFilter(request, response);
    }

}