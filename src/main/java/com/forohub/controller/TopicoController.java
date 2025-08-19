package com.forohub.controller;

import com.forohub.model.Topico;
import com.forohub.repository.TopicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private final TopicoRepository repository;

    public TopicoController(TopicoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> crear(@RequestBody Topico topico) {
        if (topico.getTitulo() == null || topico.getMensaje() == null ||
                topico.getAutor() == null || topico.getCurso() == null || topico.getStatus() == null) {
            return ResponseEntity.badRequest().body("Todos los campos son obligatorios");
        }
        if (repository.existsByTituloAndMensaje(topico.getTitulo(), topico.getMensaje())) {
            return ResponseEntity.status(409).body("Tópico duplicado");
        }
        topico.setFechaCreacion(LocalDateTime.now());
        Topico saved = repository.save(topico);
        return ResponseEntity.status(201).body(saved);
    }

    @GetMapping
    public ResponseEntity<Page<Topico>> listar(@PageableDefault(size = 10, sort = "fechaCreacion") Pageable pageable) {
        return ResponseEntity.ok(repository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Topico> maybe = repository.findById(id);
        if (maybe.isPresent()) {
            return ResponseEntity.ok(maybe.get());
        }
        return ResponseEntity.status(404).body("Tópico no encontrado");
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Topico nuevo) {
        Optional<Topico> maybe = repository.findById(id);
        if (!maybe.isPresent()) {
            return ResponseEntity.status(404).body("Tópico no encontrado");
        }
        Topico t = maybe.get();
        if (nuevo.getTitulo() != null) t.setTitulo(nuevo.getTitulo());
        if (nuevo.getMensaje() != null) t.setMensaje(nuevo.getMensaje());
        if (nuevo.getAutor() != null) t.setAutor(nuevo.getAutor());
        if (nuevo.getCurso() != null) t.setCurso(nuevo.getCurso());
        if (nuevo.getStatus() != null) t.setStatus(nuevo.getStatus());
        Topico updated = repository.save(t);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.status(404).body("Tópico no encontrado");
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
