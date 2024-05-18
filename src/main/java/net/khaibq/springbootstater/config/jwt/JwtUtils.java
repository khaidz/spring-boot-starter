package net.khaibq.springbootstater.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import net.khaibq.springbootstater.config.security.CustomUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.token.jwtSecret}")
    private String jwtSecret;

    @Value("${app.token.jwtExpirationMs}")
    private int jwtExpirationMs;

    private JwtParser jwtParser;
    private Key key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public String generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        CustomUser customUser = (CustomUser) authentication.getPrincipal();

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", customUser.getId());
        claims.put("username", authentication.getName());
        claims.put("email", customUser.getEmail());
        claims.put("roles", authorities);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(authentication.getName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get("roles").toString().split(","))
                .filter(auth -> !auth.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .toList();
        String username = null;
        Long id = null;
        String email = null;
        if (claims.get("username") != null) {
            username = (String) claims.get("username");
        }
        if (claims.get("id") != null) {
            id = Long.valueOf(String.valueOf(claims.get("id")));
        }
        if (claims.get("email") != null) {
            email = (String) claims.get("email");
        }
        CustomUser principal = new CustomUser(username, "", true,
                true, true, true, authorities, id, email);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            jwtParser.parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.info("Invalid JWT token.");
            logger.trace("Invalid JWT token trace.", e);
        }
        return false;
    }
}
