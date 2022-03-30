package com.app.disney.jwt;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.app.disney.dto.JwtDTO;
import com.app.disney.dto.MainUser;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;


@Component
public class JwtProvider {
    private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${jwt.secret}") // obtiene del propiertis
    private String secret;

    @Value("${jwt.expiration}") // obtiene del propiertis
    private int expiration;

    public String generateToken(Authentication authentication) {
        MainUser usuarioPrincipal = (MainUser) authentication.getPrincipal();
        List<String> roles = usuarioPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return Jwts.builder()
                .setId((usuarioPrincipal.getId()).toString()) // le agrego el id para poder setearlo desde angular
                .setSubject(usuarioPrincipal.getUsername())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                .compact();
    }

    public String getTelefonoFromToken(String token) {
        return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("token mal formado");
        } catch (UnsupportedJwtException e) {
            logger.error("token no soportado");
        } catch (ExpiredJwtException e) {
            logger.error("token expirado");
        } catch (IllegalArgumentException e) {
            logger.error("token vacío");
        } catch (SignatureException e) {
            logger.error("fail en la firma");
        }
        return false;
    }

    public String refreshToken(JwtDTO jwtDto) throws ParseException {
        try {
            Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(jwtDto.getToken());
        } catch (ExpiredJwtException e) {
            JWT jwt = JWTParser.parse(jwtDto.getToken());
            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            String telefono = claims.getSubject();
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) claims.getClaim("roles");
            // acá se supone que recupera el id del usuario:
            String idUser = claims.getJWTID();

            return Jwts.builder()
                    .setId(idUser)
                    .setSubject(telefono)
                    .claim("roles", roles)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(new Date().getTime() + expiration * 1000))
                    .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                    .compact();
        }
        return null;
    }
}
