package com.github.youssefwadie.spring_brotli_compression_demo.mapper;

import com.github.youssefwadie.spring_brotli_compression_demo.compression.BrotliUtil;
import com.github.youssefwadie.spring_brotli_compression_demo.dto.DocumentDTO;
import com.github.youssefwadie.spring_brotli_compression_demo.entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class DocumentMapper {
    @Autowired
    protected BrotliUtil brotliUtil;

    @Mapping(target = "content", expression = "java(brotliUtil.decompress(source.getContent()))")
    public abstract DocumentDTO toDTO(Document source);

    @Mapping(target = "content", expression = "java(brotliUtil.compress(source.getContent()))")
    public abstract Document toEntity(DocumentDTO source);
}
