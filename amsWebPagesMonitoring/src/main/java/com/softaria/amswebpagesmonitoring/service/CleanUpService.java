package com.softaria.amswebpagesmonitoring.service;

import com.softaria.amswebpagesmonitoring.repository.PagesSnapshotsColdRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Сервис для очистки холодного хранилища страниц.
 * Удаляет все снимки старше 90 дней.
 */
@Service
@Slf4j
public class CleanUpService {

    private final PagesSnapshotsColdRepository pagesSnapshotsColdRepository;

    public CleanUpService(PagesSnapshotsColdRepository pagesSnapshotsColdRepository) {
        this.pagesSnapshotsColdRepository = pagesSnapshotsColdRepository;
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void cleanUp() {
        LocalDate cutoffDate = LocalDate.now().minusDays(90);

        log.info("Clean up date: {}", cutoffDate);
        pagesSnapshotsColdRepository.deleteBySnapshotDateBefore(cutoffDate);
        log.info("Clean up completed");
    }
}
