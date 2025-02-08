package com.softaria.amswebpagesmonitoring.repository;

import com.softaria.amswebpagesmonitoring.model.MonitoringHistorySnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MonitoringHistoryRepository extends JpaRepository<MonitoringHistorySnapshot, Long> {

}
