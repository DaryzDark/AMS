package com.softaria.amswebpagesmonitoring.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "monitoring_hisory")
public class MonitoringHistorySnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "change_date", nullable = false)
    private LocalDate changeDate;

    @Type(JsonBinaryType.class)
    @Column(name = "changed_urls", columnDefinition = "jsonb")
    private List<String> changedUrls;

    @Type(JsonBinaryType.class)
    @Column(name = "deleted_urls", columnDefinition = "jsonb")
    private List<String> deletedUrls;

    @Type(JsonBinaryType.class)
    @Column(name = "added_urls", columnDefinition = "jsonb")
    private List<String> addedUrls;

    public MonitoringHistorySnapshot(LocalDate changeDate, List<String> changedUrls, List<String> deletedUrls, List<String> addedUrls) {
        this.changeDate = changeDate;
        this.changedUrls = changedUrls;
        this.deletedUrls = deletedUrls;
        this.addedUrls = addedUrls;
    }
}
