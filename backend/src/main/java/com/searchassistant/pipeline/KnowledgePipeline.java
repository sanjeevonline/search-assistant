package com.searchassistant.pipeline;

import com.searchassistant.model.DocumentChunk;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// In production, 
// 1. keywordIndex should be persisted in a distributed Inverted Index (Elasticsearch/OpenSearch) or Key-Value store (Redis) to handle millions of terms.
// 2. Ingestion should be asynchronous (Kafka/Queue) and handle large batches.
// 3. Implement robust error handling and retry logic for individual documents.
// 4. use a smarter chunking strategy (e.g., RecursiveCharacterTextSplitter, Semantic Chunking).
// 5. Handle overlapping chunks to preserve context.    
// 6. Use a distributed Vector Database (Pinecone/ Weaviate) for efficient similarity search.
// 7. Batch embedding requests to the model provider.
// 8. integrate with a real scalar embedding model (OpenAI text-embedding-3, Cohere, etc.)
public class KnowledgePipeline {
    public List<DocumentChunk> chunks = new ArrayList<>();
    public List<DocumentChunk> vectorDB = new ArrayList<>();
    public Map<String, List<DocumentChunk>> keywordIndex = new HashMap<>();

    // Step 1: ingest documents
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
        }
    }

    // Step 2c: keyword index
    private void buildKeywordIndex() {
        for (DocumentChunk chunk : chunks) {
            for (String word : chunk.text.toLowerCase().split("\\s+")) {
                // Apply stop-word removal, stemming, and tokenization.
                keywordIndex.computeIfAbsent(word, k -> new ArrayList<>()).add(chunk);
            }
        }
    }

    // Dummy embedding
    private List<Double> computeEmbedding(String text) {
        List<Double> emb = new ArrayList<>();
        for (char c : text.substring(0, Math.min(text.length(), 10)).toCharArray()) {
            emb.add((double) c);
        }
        return emb;
    }
}