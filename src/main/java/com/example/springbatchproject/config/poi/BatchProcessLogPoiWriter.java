package com.example.springbatchproject.config.poi;

import com.example.springbatchproject.entity.BatchProcessLog;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.util.List;

@Component
public class BatchProcessLogPoiWriter {

    public void writeBatchLogsToExcel(List<BatchProcessLog> batchLogs, String filePath) {
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(filePath)) {

            Sheet sheet = workbook.createSheet("배치로그");
            int rowNum = 0;

            // 헤더
            Row header = sheet.createRow(rowNum++);
            header.createCell(0).setCellValue("잡이름");
            header.createCell(1).setCellValue("시작시각");
            header.createCell(2).setCellValue("종료시각");
            header.createCell(3).setCellValue("전체건수");
            header.createCell(4).setCellValue("성공건수");
            header.createCell(5).setCellValue("실패건수");
            header.createCell(6).setCellValue("상태");
            header.createCell(7).setCellValue("메시지");

            // 본문
            for (BatchProcessLog log : batchLogs) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(log.getJobName());
                row.createCell(1).setCellValue(log.getStartedAt() != null ? log.getStartedAt().toString() : "");
                row.createCell(2).setCellValue(log.getFinishedAt() != null ? log.getFinishedAt().toString() : "");
                row.createCell(3).setCellValue(log.getTotalCount());
                row.createCell(4).setCellValue(log.getSuccessCount());
                row.createCell(5).setCellValue(log.getFailCount());
                row.createCell(6).setCellValue(log.getStatus() != null ? log.getStatus().name() : "");
                row.createCell(7).setCellValue(log.getMessage() != null ? log.getMessage() : "");
            }

            for (int i = 0; i < 8; i++) sheet.autoSizeColumn(i);
            workbook.write(fos);
        } catch (Exception e) {
            throw new RuntimeException("배치 로그 엑셀 파일 쓰기 실패: " + e.getMessage(), e);
        }
    }
}
