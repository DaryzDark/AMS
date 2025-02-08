package com.softaria.amswebpagesmonitoring.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MockSnapshotService {

    private static final String[] URLS = {
            "https://example.com/home",
            "https://example.com/about",
            "https://example.com/contact",
            "https://example.com/products",
            "https://example.com/blog",
            "https://example.com/news"
    };

    /**
     * Генерирует случайное состояние страниц.
     */
    private Map<String, String> generateSnapshots(int count, boolean simulateChanges) {
        Map<String, String> snapshots = new HashMap<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            String url = URLS[random.nextInt(URLS.length)];
            String htmlContent = "<html><body>Content " + (simulateChanges ? UUID.randomUUID() : "static") + "</body></html>";
            snapshots.put(url, htmlContent);
        }
        return snapshots;
    }

    /**
     * Заглушка для получения вчерашнего и сегодняшнего состояния веб-страниц.
     * @return Массив из двух Map: [0] - вчерашнее состояние, [1] - сегодняшнее
     * Заменить на метод получения реальных данных.
     */
    public Map<String, String>[] getYesterdayAndTodaySnapshots() {
        Map<String, String> yesterdaySnapshots = generateSnapshots(4, false); // Без изменений (стабильные данные)
        Map<String, String> todaySnapshots = generateSnapshots(5, true);  // Симуляция изменений

        return new Map[]{yesterdaySnapshots, todaySnapshots};
    }
}

