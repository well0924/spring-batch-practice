package com.example.springbatchproject.repository;

import com.example.springbatchproject.entity.BatchProcessLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchProcessLogRepository extends JpaRepository<BatchProcessLog,Long> {

}
