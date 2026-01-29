package com.searchassistant.pipeline;

import com.searchassistant.model.DocumentChunk;
import com.searchassistant.model.QueryInfo;
import com.searchassistant.service.LLM;
import com.searchassistant.service.PostChecks;

import java.util.List;
import java.util.Map;

public class QueryOrchestrator {
    KnowledgePipeline pipeline;

    public QueryOrchestrator(KnowledgePipeline pipeline) {
        this.pipeline = pipeline;
    }

    public String handleQuery(String query, Map<String, Object> userContext) {
        // 1. Preprocessing
        QueryInfo queryInfo = QueryPreprocessor.preprocess(query);
        // 2. Doc Access Filter
        List<DocumentChunk> allowedChunks = DocAccessFilter.filterChunks(pipeline.chunks, userContext);
        // 3. Retrieve
        List<DocumentChunk> candidates = Retriever.retrieve(queryInfo, allowedChunks, 5);
        // 4. Rerank
        List<DocumentChunk> reranked = Reranker.rerank(queryInfo, candidates);
        // 5. Context Packager
        String context = ContextPackager.packageContext(reranked, 3);
        // 6. LLM
        String answer = LLM.generateAnswer(context, query);
        // 7. Post-checks
        return PostChecks.validate(answer);
    }
}
