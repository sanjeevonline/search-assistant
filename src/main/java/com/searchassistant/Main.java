package com.searchassistant;

import com.searchassistant.pipeline.KnowledgePipeline;
import com.searchassistant.pipeline.QueryOrchestrator;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
        private static QueryOrchestrator orchestrator;

        public static void main(String[] args) {
                initializePipeline();

                Javalin app = Javalin.create(config -> {
                        config.bundledPlugins.enableCors(cors -> {
                                cors.addRule(it -> {
                                        it.anyHost();
                                });
                        });
                }).start(7070);

                // check server is running
                app.get("/", ctx -> ctx.result("Search Assistant API is running"));
                app.post("/api/query", Main::handleQuery);

                System.out.println("Search Assistant API running on port 7070...");
        }

        private static void handleQuery(Context ctx) {
                try {
                        Map<String, Object> body = ctx.bodyAsClass(Map.class);
                        String query = (String) body.get("query");
                        Map<String, Object> userContext = (Map<String, Object>) body.get("userContext");

                        // Default context if missing (safe fallback for demo)
                        if (userContext == null) {
                                userContext = new HashMap<>();
                                userContext.put("role", "member");
                                userContext.put("allowed_plans", List.of("Plan X"));
                                userContext.put("state", "CA");
                        }

                        String answer = orchestrator.handleQuery(query, userContext);

                        ctx.json(Map.of("answer", answer));
                } catch (Exception e) {
                        e.printStackTrace();
                        ctx.status(500).json(Map.of("error", e.getMessage()));
                }
        }

        private static void initializePipeline() {
                KnowledgePipeline pipeline = new KnowledgePipeline();

                // Ingest documents
                List<Map<String, Object>> docs = new ArrayList<>();
                docs.add(Map.of(
                                "text", "MRI is covered with prior authorization. Maximum 3 per year.",
                                "metadata", Map.of("plan_id", "Plan X", "state", "CA", "effective_date",
                                                LocalDate.of(2025, 1, 1))));
                docs.add(Map.of(
                                "text", "CT scans are covered without limits.",
                                "metadata", Map.of("plan_id", "Plan X", "state", "CA", "effective_date",
                                                LocalDate.of(2025, 1, 1))));
                docs.add(Map.of(
                                "text", "MRI coverage for Plan Y is unlimited.",
                                "metadata", Map.of("plan_id", "Plan Y", "state", "CA", "effective_date",
                                                LocalDate.of(2025, 1, 1))));
                docs.add(Map.of(
                                "text",
                                "Emergency Room visits have a $200 copay. Copay is waived if admitted to the hospital.",
                                "metadata", Map.of("plan_id", "Plan X", "state", "CA", "effective_date",
                                                LocalDate.of(2025, 1, 1))));
                docs.add(Map.of(
                                "text",
                                "Physical Therapy is covered up to 20 visits per calendar year. Authorization required.",
                                "metadata", Map.of("plan_id", "Plan X", "state", "CA", "effective_date",
                                                LocalDate.of(2025, 1, 1))));
                docs.add(Map.of(
                                "text", "Generic prescription drugs have a $10 copay for a 30-day supply.",
                                "metadata", Map.of("plan_id", "Plan X", "state", "CA", "effective_date",
                                                LocalDate.of(2025, 1, 1))));
                docs.add(Map.of(
                                "text",
                                "Mental Health outpatient visits are covered with the same copay as a Primary Care Physician visit.",
                                "metadata", Map.of("plan_id", "Plan X", "state", "CA", "effective_date",
                                                LocalDate.of(2025, 1, 1))));
                pipeline.ingest(docs);

                orchestrator = new QueryOrchestrator(pipeline);
        }
}
