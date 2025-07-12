package com.example.springbatchproject.entity;

import com.example.springbatchproject.entity.consts.BatchProcessLogStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BatchProcessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobName;

    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    private int totalCount;
    private int successCount;
    private int failCount;

    @Enumerated(EnumType.STRING)
    private BatchProcessLogStatus status; // 'SUCCESS', 'FAILED', 'PARTIAL_SUCCESS'

    @Lob
    private String message; // 상세 메시지/에러 로그 등
}
