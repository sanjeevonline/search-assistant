package com.searchassistant.pipeline;

import com.searchassistant.model.DocumentChunk;
import java.util.List;
import java.util.stream.Collectors;

public class ContextPackager {
    public static String packageContext(List<DocumentChunk> chunks, int maxPassages) {
        return chunks.stream()
                .limit(maxPassages)
                .map(c -> c.text + " (plan: " + c.metadata.get("plan_id") + ")")
                .collect(Collectors.joining("\n\n"));
    }
}
