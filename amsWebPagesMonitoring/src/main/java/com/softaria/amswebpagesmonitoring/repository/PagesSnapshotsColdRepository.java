package com.softaria.amswebpagesmonitoring.repository;

import com.softaria.amswebpagesmonitoring.model.PageSnapshot;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface PagesSnapshotsColdRepository extends JpaRepository<PageSnapshot, Long> {

    @Transactional
    void deleteBySnapshotDateBefore(LocalDate date);
}
