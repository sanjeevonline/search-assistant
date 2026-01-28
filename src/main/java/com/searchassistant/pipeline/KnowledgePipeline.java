package com.searchassistant.pipeline;

import com.searchassistant.model.DocumentChunk;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KnowledgePipeline {
    public List<DocumentChunk> chunks = new ArrayList<>();
    public List<DocumentChunk> vectorDB = new ArrayList<>();
    // TODO: Production Improvement: In production, persist this in a distributed
    // Inverted Index (Elasticsearch/OpenSearch) or Key-Value store (Redis) to
    // handle millions of terms.
    public Map<String, List<DocumentChunk>> keywordIndex = new HashMap<>();

    // Step 1: ingest documents
    // TODO: Production Improvement: In a real system, this should be asynchronous
    // (Kafka/Queue) and handle large batches.
    // TODO: Production Improvement: Implement robust error handling and retry logic
    // for individual documents.
    public void ingest(List<Map<String, Object>> docs) {
        for (Map<String, Object> doc : docs) {
            String text = (String) doc.get("text");
            Map<String, Object> metadata = (Map<String, Object>) doc.get("metadata");
            List<DocumentChunk> chunked = chunkDocument(text, metadata);
            chunks.addAll(chunked);
        }
        embedChunks();
        buildKeywordIndex();
    }

    // Step 2a: chunk document
    // TODO: Production Improvement: Use a smarter chunking strategy (e.g.,
    // RecursiveCharacterTextSplitter, Semantic Chunking).
    // TODO: Production Improvement: Handle overlapping chunks to preserve context.
    private List<DocumentChunk> chunkDocument(String text, Map<String, Object> metadata) {
        String[] paragraphs = text.split("\n");
        List<DocumentChunk> result = new ArrayList<>();
        for (String p : paragraphs) {
            if (!p.trim().isEmpty()) {
                result.add(new DocumentChunk(p, metadata));
            }
        }
        return result;
    }

    // Step 2b: embedding placeholder
    private void embedChunks() {
        for (DocumentChunk chunk : chunks) {
            chunk.embedding = computeEmbedding(chunk.text);
            vectorDB.add(chunk);
            // TODO: Production Improvement: Persist to a real Vector Database (Pinecone,
            // Weaviate, Milvus).
            // TODO: Production Improvement: Batch embedding requests to the model provider.
        }
    }

    // Step 2c: keyword index
    private void buildKeywordIndex() {
        for (DocumentChunk chunk : chunks) {
            for (String word : chunk.text.toLowerCase().split("\\s+")) {
                // TODO: Production Improvement: Use a dedicated Search Engine (Elasticsearch,
                // Solr, OpenSearch).
                // TODO: Production Improvement: Apply stop-word removal, stemming, and
                // tokenization.
                keywordIndex.computeIfAbsent(word, k -> new ArrayList<>()).add(chunk);
            }
        }
    }

    // Dummy embedding
    // TODO: Production Improvement: Integrate with a real scalar embedding model
    // (OpenAI text-embedding-3, Cohere, etc.).
    private List<Double> computeEmbedding(String text) {
        List<Double> emb = new ArrayList<>();
        for (char c : text.substring(0, Math.min(text.length(), 10)).toCharArray()) {
            emb.add((double) c);
        }
        return emb;
    }
}
