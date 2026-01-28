package com.searchassistant.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PostChecksTest {

    @Test
    void testPHIRedaction() {
        String input = "The member SSN is 123-45-6789.";
        String result = PostChecks.validate(input);
        assertEquals("[REDACTED]", result);
    }

    @Test
    void testConfidentialKeyword() {
        String input = "This is strictly confidential information.";
        String result = PostChecks.validate(input);
        assertEquals("[REDACTED]", result);
    }

    @Test
    void testToneCheck() {
        String input = "I don't know the answer.";
        String result = PostChecks.validate(input);
        // Corrected expectation
        assertTrue(result.contains("unable to provide a definitive answer"));
    }

    @Test
    void testDisclaimerAppend() {
        String input = "MRI is covered.";
        String result = PostChecks.validate(input);
        assertTrue(result.contains("MRI is covered."));
        assertTrue(result.contains("Verify specific benefits with Member Services"));
    }

    @Test
    void testSafePassThrough() {
        String input = "The sky is blue.";
        String result = PostChecks.validate(input);
        assertEquals("The sky is blue.", result);
    }

    @Test
    void testNullOrEmpty() {
        String result = PostChecks.validate("");
        assertTrue(result.contains("unable to provide a definitive answer"));
    }
}
