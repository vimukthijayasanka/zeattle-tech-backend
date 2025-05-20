package lk.ijse.dep13.zeattle_tech.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lk.ijse.dep13.zeattle_tech.security.user.ShopUserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {
    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;
    @Value("${auth.token.expirationInMils}")
    private int expirationTime;

    public String generateTokenFor(Authentication authentication) {
        ShopUserDetailsImpl userPrincipal = (ShopUserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("id", userPrincipal.getId())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expirationTime))
                .signWith(key(), SignatureAlgorithm.HS256).compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException |
                 io.jsonwebtoken.security.SecurityException | IllegalArgumentException e) {
            throw new JwtException(e.getMessage());
        }
    }
}
