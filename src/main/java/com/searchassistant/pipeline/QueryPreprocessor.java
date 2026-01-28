package com.searchassistant.pipeline;

import com.searchassistant.model.QueryInfo;
import java.util.ArrayList;

public class QueryPreprocessor {
    public static QueryInfo preprocess(String query) {
        QueryInfo info = new QueryInfo(query);
        info.entities.put("procedures", new ArrayList<>());
        info.entities.put("plans", new ArrayList<>());

        if (info.normalizedQuery.contains("mri")) {
            info.entities.get("procedures").add("MRI");
        }
        if (info.normalizedQuery.contains("plan x")) {
            info.entities.get("plans").add("Plan X");
        }
        if (info.normalizedQuery.contains("limit") || info.normalizedQuery.contains("how many")) {
            info.intent.add("limits");
        } else {
            info.intent.add("coverage");
        }
        return info;
    }
}
