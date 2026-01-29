package com.searchassistant.pipeline;

import com.searchassistant.model.DocumentChunk;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DocAccessFilter {
    public static List<DocumentChunk> filterChunks(List<DocumentChunk> chunks, Map<String, Object> userContext) {
        List<String> allowedPlans = (List<String>) userContext.get("allowed_plans");
        LocalDate today = LocalDate.now();
        return chunks.stream().filter(chunk -> {
            String planId = (String) chunk.metadata.get("plan_id");
            LocalDate effectiveDate = (LocalDate) chunk.metadata.getOrDefault("effective_date", today);
            return allowedPlans.contains(planId) && !effectiveDate.isAfter(today);
        }).collect(Collectors.toList());
    }
}
