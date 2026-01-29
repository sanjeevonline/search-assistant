package com.searchassistant.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryInfo {
    public String normalizedQuery;
    public Map<String, List<String>> entities;
    public List<String> intent;

    public QueryInfo(String query) {
        this.normalizedQuery = query.toLowerCase().trim();
        this.entities = new HashMap<>();
        this.intent = new ArrayList<>();
    }
}
