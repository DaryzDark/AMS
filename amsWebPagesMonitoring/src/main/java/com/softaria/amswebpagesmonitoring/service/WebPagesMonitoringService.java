package com.softaria.amswebpagesmonitoring.service;

import com.softaria.amswebpagesmonitoring.model.MonitoringHistorySnapshot;
import com.softaria.amswebpagesmonitoring.model.PageSnapshot;
import com.softaria.amswebpagesmonitoring.repository.MonitoringHistoryRepository;
import com.softaria.amswebpagesmonitoring.repository.PagesSnapshotsColdRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WebPagesMonitoringService {

    private final MockSnapshotService mockSnapshotService;
    private final MonitoringHistoryRepository monitoringHistoryRepository;
    private final PagesSnapshotsColdRepository pagesSnapshotsColdRepository;

    public WebPagesMonitoringService(MockSnapshotService mockSnapshotService,
                                     MonitoringHistoryRepository monitoringHistoryRepository,
                                     PagesSnapshotsColdRepository pageSnapshotsColdRepository) {
        this.mockSnapshotService = mockSnapshotService;
        this.monitoringHistoryRepository = monitoringHistoryRepository;
        this.pagesSnapshotsColdRepository = pageSnapshotsColdRepository;
    }

    /**
     * Основной метод мониторинга
     */
    @Scheduled(cron = "0 0 9 * * *") // Запуск в 9:00 утра
    public void monitorWebPages() {
        Map<String, String>[] snapshots = mockSnapshotService.getYesterdayAndTodaySnapshots();
        Map<String, String> yesterdaySnapshots = snapshots[0];
        Map<String, String> todaySnapshots = snapshots[1];

        // Найти разницу между вчера и сегодня
        List<String> disappearedUrls = findDisappearedPages(yesterdaySnapshots, todaySnapshots);
        List<String> newUrls = findNewPages(yesterdaySnapshots, todaySnapshots);
        List<String> changedUrls = findChangedPages(yesterdaySnapshots, todaySnapshots);

        // Сохранение изменений в БД
        saveChanges(disappearedUrls, newUrls, changedUrls);

        // Сохранение сегодняшних снимков в "холодное хранилище"
        saveTodaySnapshots(todaySnapshots);

        // Формирование и отправка email-уведомления
        String emailBody = generateEmail(disappearedUrls, newUrls, changedUrls);
        sendEmailNotification(emailBody);
    }



    /**
     * Найти исчезнувшие страницы (были вчера, но их нет сегодня)
     */
    public List<String> findDisappearedPages(Map<String, String> yesterday, Map<String, String> today) {
        return yesterday.keySet().stream()
                .filter(url -> !today.containsKey(url))
                .collect(Collectors.toList());
    }

    /**
     * Метода нахождения новых страниц
     */
    public List<String> findNewPages(Map<String, String> yesterday, Map<String, String> today) {
        return today.keySet().stream()
                .filter(url -> !yesterday.containsKey(url))
                .collect(Collectors.toList());
    }

    /**
     * Метода нахождения измененных страниц
     */
    public List<String> findChangedPages(Map<String, String> yesterday, Map<String, String> today) {
        return today.keySet().stream()
                .filter(url -> yesterday.containsKey(url) && !Objects.equals(yesterday.get(url), today.get(url)))
                .collect(Collectors.toList());
    }

    /**
     * Сохранить отчет в БД
     */
    public void saveChanges(List<String> disappeared, List<String> newPages, List<String> changed) {
        MonitoringHistorySnapshot monitoringHistorySnapshot = new MonitoringHistorySnapshot(
                LocalDate.now(),
                changed,
                disappeared,
                newPages
        );
        monitoringHistoryRepository.save(monitoringHistorySnapshot);
    }

    /**
     * Сохранить сегодняшние снапшоты в холодное хранилище
     */
    public void saveTodaySnapshots(Map<String, String> todaySnapshots) {
        LocalDate today = LocalDate.now();
        todaySnapshots.forEach((url, html) -> {
            PageSnapshot snapshot = new PageSnapshot(url, today, html);
            pagesSnapshotsColdRepository.save(snapshot);
        });
    }

    /**
     * Генерация email-сообщения по шаблону
     */
    public String generateEmail(List<String> disappeared, List<String> newPages, List<String> changed) {
        return String.format("""
                Здравствуйте, дорогая и.о. секретаря,

                За последние сутки во вверенных Вам сайтах произошли следующие изменения:

                Исчезли следующие страницы:
                %s

                Появились следующие новые страницы:
                %s

                Изменились следующие страницы:
                %s

                С уважением,
                автоматизированная система мониторинга.
                """,
                formatList(disappeared),
                formatList(newPages),
                formatList(changed)
        );
    }

    /**
     * Вспомогательный метод для форматирования
     */
    private String formatList(List<String> urls) {
        return urls.isEmpty() ? "Нет изменений" : String.join("\n", urls);
    }

    /**
     * Метод-заглушка для отправки email
     * В реальном проекте заменяется на EmailService
     */
    private void sendEmailNotification(String emailBody) {
        log.info("=== Уведомление отправлено ===");
        log.info(emailBody);
    }
}
