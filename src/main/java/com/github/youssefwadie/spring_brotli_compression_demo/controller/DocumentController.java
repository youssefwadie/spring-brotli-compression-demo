package com.github.youssefwadie.spring_brotli_compression_demo.controller;

import com.github.youssefwadie.spring_brotli_compression_demo.dto.ApiError;
import com.github.youssefwadie.spring_brotli_compression_demo.dto.DocumentDTO;
import com.github.youssefwadie.spring_brotli_compression_demo.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Function;


@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> save(@RequestBody Mono<DocumentDTO> documentDTOMono) {
        return documentService.create(documentDTOMono)
                .<ResponseEntity<Object>>map(documentDTO -> ResponseEntity.created(URI.create("/api/v1/documents/" + documentDTO.getId())).body(documentDTO))
                .onErrorResume(this::handleError);
    }

    @PutMapping
    public Mono<ResponseEntity<Object>> update(@RequestBody Mono<DocumentDTO> documentDTOMono) {
        return documentService.update(documentDTOMono)
                .<ResponseEntity<Object>>map(documentDTO -> ResponseEntity.ok().body(documentDTO))
                .onErrorResume(this::handleError)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<Object>> getById(@PathVariable("id") String id) {
        return documentService.findById(id)
                .<ResponseEntity<Object>>map(documentDTO -> ResponseEntity.ok().body(documentDTO))
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
    }

    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Boolean>> delete(@PathVariable("id") String id) {
        return documentService.deleteById(id)
                .map(deleted -> ResponseEntity.noContent().build());
    }

    private Mono<ResponseEntity<Object>> handleError(Throwable throwable) {
        return Mono.just(ResponseEntity.badRequest().body(ApiError.badRequest(throwable.getMessage())));
    }
}
