package ru.sereda.autowiretest.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET = "2442264529482B4D6251655468576D5A7134743777217A25432A462D4A614E63";

    public boolean isTokenValid(String token){
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token ){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public List<GrantedAuthority> getAuthorities(String token){
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : extractRoles(token)){
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    public List<String> extractRoles(String token){
        return extractAllClaims(token).get("roles",List.class);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
    }
}