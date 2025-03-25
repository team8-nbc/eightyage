package com.example.eightyage.domain.search.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class SearchLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;
    private LocalDateTime searchedAt;

    public static SearchLog of(String keyword) {
        SearchLog log = new SearchLog();
        log.keyword = keyword;
        log.searchedAt = LocalDateTime.now();
        return log;
    }
}
