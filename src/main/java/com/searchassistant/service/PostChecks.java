package com.searchassistant.service;

public class PostChecks {
    private static final String DISCLAIMER = "\n\nNote: Verify specific benefits with Member Services.";
    private static final String REDACTED = "[REDACTED]";
    private static final String FALLBACK_MSG = "I am unable to provide a definitive answer. Please consult a representative.";

    public static String validate(String answer) {
        if (answer == null || answer.isEmpty()) {
            return FALLBACK_MSG;
        }

        // 1. PHI/PII Check (Simple regex for SSN-like patterns)
        // Matches \d{3}-\d{2}-\d{4}
        if (answer.matches(".*\\b\\d{3}-\\d{2}-\\d{4}\\b.*")) {
            return REDACTED;
        }

        // Keyword check
        if (answer.toLowerCase().contains("confidential") ||
                answer.toLowerCase().contains("private")) {
            return REDACTED;
        }

        // 2. Tone Check
        if (answer.toLowerCase().contains("i don't know") ||
                answer.toLowerCase().contains("unsure")) {
            return FALLBACK_MSG;
        }

        // 3. Disclaimer Append
        String lowerAns = answer.toLowerCase();
        if (lowerAns.contains("covered") ||
                lowerAns.contains("limit") ||
                lowerAns.contains("mri") ||
                lowerAns.contains("surgery") ||
                lowerAns.contains("therapy")) {
            return answer + DISCLAIMER;
        }

        return answer;
    }
}
