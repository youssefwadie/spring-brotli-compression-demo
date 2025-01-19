package com.github.youssefwadie.spring_brotli_compression_demo.service;

import com.github.youssefwadie.spring_brotli_compression_demo.dto.DocumentDTO;
import com.github.youssefwadie.spring_brotli_compression_demo.entity.Document;
import com.github.youssefwadie.spring_brotli_compression_demo.mapper.DocumentMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DocumentService {
    private static final Map<String, Document> documents
            = new ConcurrentHashMap<>();

    private final DocumentMapper mapper;

    public DocumentService(DocumentMapper mapper) {
        this.mapper = mapper;
    }

    public Mono<DocumentDTO> findById(String id) {
        Objects.requireNonNull(id, "id");
        return Mono.justOrEmpty(documents.get(id))
                .map(mapper::toDTO)
                .publishOn(Schedulers.boundedElastic());
    }

    public Mono<DocumentDTO> update(Mono<DocumentDTO> documentDTOMono) {
        return documentDTOMono
                .doOnNext(documentDTO -> Objects.requireNonNull(documentDTO.getId(), "id"))
                .flatMap(documentDTO -> {
                    return Mono.create((MonoSink<Document> monoSink) -> {
                                if (Objects.isNull(documentDTO.getId())) {
                                    monoSink.error(new IllegalArgumentException("id cannot be null"));
                                } else {
                                    Document document = documents.get(documentDTO.getId());
                                    if (Objects.isNull(document)) {
                                        monoSink.success();
                                    } else {
                                        monoSink.success(document);
                                    }
                                }
                            })
                            .map(mapper::toDTO)
                            .publishOn(Schedulers.boundedElastic());
                })
                .flatMap(this::handleSave);
    }

    public Mono<DocumentDTO> create(Mono<DocumentDTO> documentDTOMono) {
        return documentDTOMono
                .doOnNext(documentDTO -> {
                    if (documentDTO.getId() != null) {
                        throw new IllegalArgumentException("id is already set");
                    }
                    documentDTO.setId(UUID.randomUUID().toString());
                })
                .flatMap(this::handleSave);
    }

    private Mono<DocumentDTO> handleSave(DocumentDTO dto) {
        return Mono.just(dto)
                .flatMap(documentDTO ->
                        Mono.fromCallable(() -> mapper.toEntity(documentDTO)).subscribeOn(Schedulers.boundedElastic()))
                .doOnNext(document -> documents.put(document.getId(), document))
                .flatMap(document ->
                        Mono.fromCallable(() -> mapper.toDTO(document)).subscribeOn(Schedulers.boundedElastic()));
    }

    public Mono<Boolean> deleteById(String id) {
        Objects.requireNonNull(id, "id");
        return Mono.justOrEmpty(documents.get(id))
                .map(document -> true)
                .switchIfEmpty(Mono.just(false));
    }
}
