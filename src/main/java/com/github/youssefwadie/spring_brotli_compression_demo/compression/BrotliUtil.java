package com.github.youssefwadie.spring_brotli_compression_demo.compression;

import com.aayushatharva.brotli4j.Brotli4jLoader;
import com.aayushatharva.brotli4j.decoder.Decoder;
import com.aayushatharva.brotli4j.encoder.Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
public class BrotliUtil {
    public static final Charset CHARSET = StandardCharsets.UTF_8;
    public static final Logger log = LoggerFactory.getLogger(BrotliUtil.class);

    static {
        // Load the native library
        Brotli4jLoader.ensureAvailability();
    }

    public ByteBuffer compress(final String input) {
        Objects.requireNonNull(input, "input");
        try {
            log.info("Compressing {}", input);
            byte[] compressed = Encoder.compress(input.getBytes(CHARSET));
            return ByteBuffer.wrap(compressed);
        } catch (IOException ex) {
            log.error("Error compressing {}", input, ex);
            throw new RuntimeException(ex);
        }
    }

    public String decompress(final ByteBuffer compressed) {
        Objects.requireNonNull(compressed, "compressed");
        try {
            log.info("Decompressing {} bytes", compressed.capacity());
            byte[] decompressedData = Decoder.decompress(compressed.array()).getDecompressedData();
            return new String(decompressedData, CHARSET);
        } catch (IOException ex) {
            log.error("Error decompressing {}", compressed.capacity(), ex);
            throw new RuntimeException(ex);
        }
    }
}
