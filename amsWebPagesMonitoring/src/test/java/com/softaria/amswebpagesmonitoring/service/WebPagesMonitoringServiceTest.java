package com.softaria.amswebpagesmonitoring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WebPagesMonitoringServiceTest {


    @InjectMocks
    private WebPagesMonitoringService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindDisappearedPages() {
        Map<String, String> yesterday = Map.of(
                "https://site1.com", "<html>old content</html>",
                "https://site2.com", "<html>old content</html>"
        );
        Map<String, String> today = Map.of(
                "https://site2.com", "<html>old content</html>"
        );

        List<String> disappeared = service.findDisappearedPages(yesterday, today);
        assertEquals(1, disappeared.size());
        assertTrue(disappeared.contains("https://site1.com"));
    }

    @Test
    void testFindNewPages() {
        Map<String, String> yesterday = Map.of(
                "https://site1.com", "<html>old content</html>"
        );
        Map<String, String> today = Map.of(
                "https://site1.com", "<html>old content</html>",
                "https://site2.com", "<html>new content</html>"
        );

        List<String> newPages = service.findNewPages(yesterday, today);
        assertEquals(1, newPages.size());
        assertTrue(newPages.contains("https://site2.com"));
    }

    @Test
    void testFindChangedPages() {
        Map<String, String> yesterday = Map.of(
                "https://site1.com", "<html>old content</html>"
        );
        Map<String, String> today = Map.of(
                "https://site1.com", "<html>new content</html>"
        );

        List<String> changedPages = service.findChangedPages(yesterday, today);
        assertEquals(1, changedPages.size());
        assertTrue(changedPages.contains("https://site1.com"));
    }

    @Test
    void testGenerateEmail() {
        List<String> disappeared = List.of("https://site1.com");
        List<String> newPages = List.of("https://site2.com");
        List<String> changed = List.of("https://site3.com");

        String email = service.generateEmail(disappeared, newPages, changed);
        assertTrue(email.contains("https://site1.com"));
        assertTrue(email.contains("https://site2.com"));
        assertTrue(email.contains("https://site3.com"));
    }


}