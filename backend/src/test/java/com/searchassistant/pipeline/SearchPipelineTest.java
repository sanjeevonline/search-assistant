package com.searchassistant.pipeline;

import com.searchassistant.model.DocumentChunk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SearchPipelineTest {

    private KnowledgePipeline pipeline;
    private QueryOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        pipeline = new KnowledgePipeline();
        orchestrator = new QueryOrchestrator(pipeline);
        generateMockData();
    }

    private void generateMockData() {
        List<Map<String, Object>> docs = new ArrayList<>();

        // 1. MRI Data (Limit checks)
        docs.add(Map.of(
                "text", "MRI of the knee is covered. Authorization required.",
                "metadata",
                Map.of("plan_id", "Plan Gold", "state", "CA", "effective_date", LocalDate.now().minusDays(10))));
        docs.add(Map.of(
                "text", "MRI limit is 2 per year for Silver members.",
                "metadata",
                Map.of("plan_id", "Plan Silver", "state", "CA", "effective_date", LocalDate.now().minusDays(10))));

        // 2. Physical Therapy (Relevance check)
        docs.add(Map.of(
                "text", "Physical therapy visits are capped at 20 per year.",
                "metadata",
                Map.of("plan_id", "Plan Gold", "state", "NY", "effective_date", LocalDate.now().minusDays(10))));

        // 3. Pharmacy (Plan exclusivity)
        docs.add(Map.of(
                "text", "Generic drugs have $0 copay.",
                "metadata",
                Map.of("plan_id", "Plan Silver", "state", "TX", "effective_date", LocalDate.now().minusDays(5))));
        docs.add(Map.of(
                "text", "Brand name drugs have 20% coinsurance.",
                "metadata",
                Map.of("plan_id", "Plan Gold", "state", "TX", "effective_date", LocalDate.now().minusDays(5))));

        // 4. Future Effective Date (Filtering check)
        docs.add(Map.of(
                "text", "New experimental treatment will be covered starting next year.",
                "metadata",
                Map.of("plan_id", "Plan Gold", "state", "CA", "effective_date", LocalDate.now().plusYears(1))));

        pipeline.ingest(docs);
    }

    @Test
    void testIngestionAndKeyWordIndex() {
        // Total active docs = 5. Future doc = 1.
        // Ingestion splits chunks. Assuming 1 chunk per doc for these short texts.
        assertEquals(6, pipeline.chunks.size());

        // Check keyword index
        assertTrue(pipeline.keywordIndex.containsKey("mri"));
        assertTrue(pipeline.keywordIndex.containsKey("therapy"));
    }

    @Test
    void testDocAccessFilter() {
        Map<String, Object> userContext = new HashMap<>();
        userContext.put("allowed_plans", List.of("Plan Gold"));

        // Should return Gold data (active)
        // Gold docs: MRI knee, PT visits, Brand drugs. (3 docs)
        // Future doc (Gold) should be excluded.
        List<DocumentChunk> filtered = DocAccessFilter.filterChunks(pipeline.chunks, userContext);

        long goldDocs = filtered.stream()
                .filter(c -> c.metadata.get("plan_id").equals("Plan Gold"))
                .count();

        assertEquals(3, goldDocs, "Should have 3 active Gold Plan documents");

        // Verify no Silver plan data
        boolean hasSilver = filtered.stream()
                .anyMatch(c -> c.metadata.get("plan_id").equals("Plan Silver"));
        assertFalse(hasSilver, "Should not contain Silver Plan data");
    }

    @Test
    void testRetrievalRelevance() {
        // Context: Gold Plan
        Map<String, Object> userContext = new HashMap<>();
        userContext.put("allowed_plans", List.of("Plan Gold"));

        // Query: "physical therapy limit"
        String query = "physical therapy limit";
        String answer = orchestrator.handleQuery(query, userContext);

        System.out.println("Test Retrieval Answer: " + answer);

        // Check if the answer contains relevant context
        // Note: The LLM placeholder output simulates the prompt.
        // We verify the context string within the answer contains the relevant text.
        assertTrue(answer.contains("Physical therapy visits are capped at 20 per year"),
                "Context should contain the PT document text");
    }

    @Test
    void testOrchestratorEndToEnd() {
        // Scenario: Silver member asking about MRI
        Map<String, Object> userContext = new HashMap<>();
        userContext.put("allowed_plans", List.of("Plan Silver"));

        String query = "What is the MRI limit?";
        String answer = orchestrator.handleQuery(query, userContext);

        System.out.println("Test EndToEnd Answer: " + answer);

        assertTrue(answer.contains("MRI limit is 2 per year"), "Should find Silver plan MRI limit");
        assertFalse(answer.contains("MRI of the knee is covered"), "Should NOT find Gold plan MRI coverage");
    }
}
