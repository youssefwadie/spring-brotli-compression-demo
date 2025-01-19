package com.github.youssefwadie.spring_brotli_compression_demo.entity;

import java.nio.ByteBuffer;
import java.util.Objects;

public final class Document {
    private String id;
    private ByteBuffer content;

    public String getId() {
        return id;
    }

    public ByteBuffer getContent() {
        return content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(ByteBuffer content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Document) obj;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CompressedDocument[" +
                "id=" + id + ", " +
                "content=" + content + ']';
    }
}

