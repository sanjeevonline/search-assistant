package com.searchassistant.model;

import java.util.List;
import java.util.Map;

public class DocumentChunk {
    public String text;
    public Map<String, Object> metadata;
    public List<Double> embedding;

    public DocumentChunk(String text, Map<String, Object> metadata) {
        this.text = text;
        this.metadata = metadata;
        this.embedding = null; // placeholder for embedding
    }
}
