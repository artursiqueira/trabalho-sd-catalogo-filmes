package br.ufes.catalogo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import br.ufes.catalogo.model.Usuario;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {
    
    private static final String SECRET = "ChaveSecretaSuperSeguraParaOCatalogoDeFilmesUFES2025SistemasDistribuidos";
    private static final long EXPIRATION_TIME = 86400000; // 24 horas em milissegundos
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    public static String gerarToken(Usuario usuario) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(usuario.getUsername())
                .claim("userId", usuario.getId())
                .claim("tipo", usuario.getTipo().name())
                .setIssuedAt(agora)
                .setExpiration(expiracao)
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims validarToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Token inválido ou expirado", e);
        }
    }

    public static String getUsernameFromToken(String token) {
        Claims claims = validarToken(token);
        return claims.getSubject();
    }

    public static Long getUserIdFromToken(String token) {
        Claims claims = validarToken(token);
        return claims.get("userId", Long.class);
    }

    public static boolean isTokenValido(String token) {
        try {
            validarToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
