package com.searchassistant.pipeline;

import com.searchassistant.model.DocumentChunk;
import com.searchassistant.model.QueryInfo;
import java.util.*;
import java.util.stream.Collectors;

public class Retriever {
    public static List<DocumentChunk> retrieve(QueryInfo queryInfo, List<DocumentChunk> chunks, int topK) {
        Set<DocumentChunk> keywordHits = new HashSet<>();
        for (String word : queryInfo.normalizedQuery.split("\\s+")) {
            // Production Improvement: Use inverted index lookup
            // (Lucene/Elasticsearch) instead of iterating chunks.
            for (DocumentChunk chunk : chunks) {
                if (chunk.text.toLowerCase().contains(word)) {
                    keywordHits.add(chunk);
                }
            }
        }

        // Vector similarity placeholder
        List<DocumentChunk> vectorHits = chunks.stream()
                .sorted(Comparator.comparingDouble(c -> -similarity(queryInfo.normalizedQuery, c.text)))
                .limit(topK)
                .collect(Collectors.toList());

        // Merge hits
        List<DocumentChunk> combined = new ArrayList<>(keywordHits);
        for (DocumentChunk c : vectorHits) {
            if (!combined.contains(c))
                combined.add(c);
        }

        return combined.stream().limit(topK).collect(Collectors.toList());
    }

    private static double similarity(String query, String text) {
        // Production Improvement will use embeddingapi like openai
        long matches = Arrays.stream(query.split("\\s+")).filter(text.toLowerCase()::contains).count();
        return (double) matches / Math.max(query.split("\\s+").length, 1);
    }
}
