package com.searchassistant.pipeline;

import com.searchassistant.model.DocumentChunk;
import com.searchassistant.model.QueryInfo;
import java.util.*;
import java.util.stream.Collectors;

public class Retriever {
    private static final Set<String> STOP_WORDS = Set.of(
            "is", "the", "a", "an", "and", "or", "but", "what", "how", "much", "for", "with", "this", "that", "it");

    public static List<DocumentChunk> retrieve(QueryInfo queryInfo, List<DocumentChunk> chunks, int topK) {
        Map<DocumentChunk, Double> scores = new HashMap<>();
        String[] words = queryInfo.normalizedQuery.split("\\s+");

        for (DocumentChunk chunk : chunks) {
            double keywordScore = 0;
            String textLower = chunk.text.toLowerCase();

            for (String word : words) {
                if (STOP_WORDS.contains(word))
                    continue;
                if (textLower.contains(word)) {
                    keywordScore += 1.0;
                }
            }

            double vectorScore = similarity(queryInfo.normalizedQuery, chunk.text);
            scores.put(chunk, keywordScore + (vectorScore * 2.0)); // Weight vector similarity more
        }

        return scores.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(topK)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private static double similarity(String query, String text) {
        String[] queryWords = query.split("\\s+");
        String textLower = text.toLowerCase();
        long matches = Arrays.stream(queryWords)
                .filter(w -> !STOP_WORDS.contains(w))
                .filter(textLower::contains)
                .count();
        return (double) matches / Math.max(queryWords.length, 1);
    }
}
