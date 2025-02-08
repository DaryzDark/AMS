package com.softaria.amswebpagesmonitoring.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pages_snapshots_cold")
public class PageSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "snapshot_date", nullable = false)
    private LocalDate snapshotDate;

    @Lob
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    public PageSnapshot(String url, LocalDate snapshotDate, String content) {
        this.url = url;
        this.snapshotDate = snapshotDate;
        this.content = content;
    }
}
