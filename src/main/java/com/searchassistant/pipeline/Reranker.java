package com.searchassistant.pipeline;

import com.searchassistant.model.DocumentChunk;
import com.searchassistant.model.QueryInfo;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Reranker {
    public static List<DocumentChunk> rerank(QueryInfo queryInfo, List<DocumentChunk> candidates) {
        List<Map.Entry<DocumentChunk, Integer>> scored = new ArrayList<>();
        for (DocumentChunk c : candidates) {
            int score = 0;
            for (String word : queryInfo.normalizedQuery.split("\\s+")) {
                if (c.text.toLowerCase().contains(word))
                    score++;
            }
            for (String plan : queryInfo.entities.getOrDefault("plans", new ArrayList<>())) {
                if (plan.equals(c.metadata.get("plan_id")))
                    score += 2;
            }
            scored.add(new AbstractMap.SimpleEntry<>(c, score));
        }
        scored.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));
        return scored.stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }
}
