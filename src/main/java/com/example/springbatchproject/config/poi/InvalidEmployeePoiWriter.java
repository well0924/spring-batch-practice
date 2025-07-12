package com.example.springbatchproject.config.poi;

import com.example.springbatchproject.entity.InvalidEmployee;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.util.List;

@Component
public class InvalidEmployeePoiWriter {

    public void writeInvalidEmployeesToExcel(List<InvalidEmployee> invalidList, String filePath) {
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(filePath)) {

            Sheet sheet = workbook.createSheet("오류데이터");
            int rowNum = 0;

            // 헤더
            Row header = sheet.createRow(rowNum++);
            header.createCell(0).setCellValue("사번");
            header.createCell(1).setCellValue("이름");
            header.createCell(2).setCellValue("부서");
            header.createCell(3).setCellValue("급여");
            header.createCell(4).setCellValue("상태");
            header.createCell(5).setCellValue("오류사유");
            header.createCell(6).setCellValue("발생일");

            // 본문
            for (InvalidEmployee emp : invalidList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(emp.getEmployeeNumber());
                row.createCell(1).setCellValue(emp.getName());
                row.createCell(2).setCellValue(emp.getDepartment());
                row.createCell(3).setCellValue(emp.getSalary() != null ? emp.getSalary() : 0);
                row.createCell(4).setCellValue(emp.getStatus() != null ? emp.getStatus() : "");
                row.createCell(5).setCellValue(emp.getErrorReason() != null ? emp.getErrorReason() : "");
                row.createCell(6).setCellValue(emp.getCreatedAt() != null ? emp.getCreatedAt().toString() : "");
            }

            for (int i = 0; i < 7; i++) sheet.autoSizeColumn(i);
            workbook.write(fos);
        } catch (Exception e) {
            throw new RuntimeException("오류 엑셀 파일 쓰기 실패: " + e.getMessage(), e);
        }
    }

}
