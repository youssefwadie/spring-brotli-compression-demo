package com.github.youssefwadie.spring_brotli_compression_demo.dto;

import java.util.Objects;

public class DocumentDTO {
    private String id;
    private String content;

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DocumentDTO) obj;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DocumentContentDTO[" +
                "id=" + id + ", " +
                "content=" + content + ']';
    }
}
