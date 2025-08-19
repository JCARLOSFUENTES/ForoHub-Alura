package com.forohub.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "cambia_esta_clave_super_secreta";
    private static final long EXPIRATION = 1000 * 60 * 60; // 1h

    private static Algorithm algorithm() {
        return Algorithm.HMAC256(SECRET);
    }

    public static String generarToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION))
                .sign(algorithm());
    }

    public static String validarYObtenerUsuario(String token) {
        JWTVerifier verifier = JWT.require(algorithm()).build();
        DecodedJWT decoded = verifier.verify(token);
        return decoded.getSubject();
    }
}
