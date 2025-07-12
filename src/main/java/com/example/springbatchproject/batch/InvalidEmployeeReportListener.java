package com.example.springbatchproject.batch;

import com.example.springbatchproject.config.poi.InvalidEmployeePoiWriter;
import com.example.springbatchproject.entity.InvalidEmployee;
import com.example.springbatchproject.repository.InvalidEmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvalidEmployeeReportListener implements JobExecutionListener {

    private final InvalidEmployeeRepository invalidEmployeeRepository;
    private final InvalidEmployeePoiWriter invalidEmployeePoiWriter;

    @Override
    public void afterJob(JobExecution jobExecution) {
        // 배치 실행이 끝난 후에 동작
        try {

            File dir = new File("output");

            if (!dir.exists()) {
                dir.mkdirs();
            }

            // (batchLogId를 JobParameter 등에서 받아올 수도 있음)
            List<InvalidEmployee> invalidList = invalidEmployeeRepository.findAll();

            // 파일명에 타임스탬프 붙이면 덮어쓰기 방지
            String outputPath = "output/invalid_employee_" + System.currentTimeMillis() + ".xlsx";
            invalidEmployeePoiWriter.writeInvalidEmployeesToExcel(invalidList, outputPath);

            log.info("배치 오류 리포트 엑셀 파일 생성 완료: {}", outputPath);
        } catch (Exception e) {
            log.error("오류 리포트 엑셀 생성 실패", e);
        }
    }
}
