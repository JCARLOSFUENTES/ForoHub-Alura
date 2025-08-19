package com.forohub.controller;

import com.forohub.model.Usuario;
import com.forohub.repository.UsuarioRepository;
import com.forohub.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository repository;
    private final BCryptPasswordEncoder encoder;

    public AuthController(UsuarioRepository repository, BCryptPasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        if (usuario.getUsername() == null || usuario.getPassword() == null) {
            return new ResponseEntity<>("username y password son obligatorios", HttpStatus.BAD_REQUEST);
        }
        if (repository.findByUsername(usuario.getUsername()).isPresent()) {
            return new ResponseEntity<>("Usuario ya existe", HttpStatus.CONFLICT);
        }
        usuario.setPassword(encoder.encode(usuario.getPassword()));
        repository.save(usuario);
        return new ResponseEntity<>("Usuario registrado", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        Optional<Usuario> maybeUser = repository.findByUsername(usuario.getUsername());

        if (!maybeUser.isPresent() || !encoder.matches(usuario.getPassword(), maybeUser.get().getPassword())) {
            return new ResponseEntity<>("Credenciales inválidas", HttpStatus.UNAUTHORIZED);
        }

        // Creamos el token en un Map tradicional
        Map<String, String> response = new HashMap<>();
        response.put("token", JwtUtil.generarToken(maybeUser.get().getUsername()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
