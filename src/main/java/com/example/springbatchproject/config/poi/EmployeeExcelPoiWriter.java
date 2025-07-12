package com.example.springbatchproject.config.poi;

import com.example.springbatchproject.entity.Employee;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.util.List;

@Component
public class EmployeeExcelPoiWriter {

    public void writeEmployeesToExcel(List<Employee> employees, String filePath) {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(filePath)) {

            Sheet sheet = workbook.createSheet("직원목록");
            int rowNum = 0;

            // 헤더
            Row header = sheet.createRow(rowNum++);
            header.createCell(0).setCellValue("사번");
            header.createCell(1).setCellValue("이름");
            header.createCell(2).setCellValue("부서");
            header.createCell(3).setCellValue("급여");
            header.createCell(4).setCellValue("입사일");
            header.createCell(5).setCellValue("퇴사일");
            header.createCell(6).setCellValue("상태");
            // 본문
            for (Employee emp : employees) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(emp.getEmployeeNumber());
                row.createCell(1).setCellValue(emp.getName());
                row.createCell(2).setCellValue(emp.getDepartment());
                row.createCell(3).setCellValue(emp.getSalary());
                row.createCell(4).setCellValue(emp.getHireDate());
                row.createCell(5).setCellValue(emp.getRetireDate());
                row.createCell(6).setCellValue(emp.getStatus().name());

            }

            //for (int i = 0; i < 5; i++) sheet.autoSizeColumn(i);
            for (int i = 0; i < 7; i++) sheet.setColumnWidth(i, 6000);
            workbook.write(fos);
        } catch (Exception e) {
            throw new RuntimeException("엑셀 파일 쓰기 실패: " + e.getMessage(), e);
        }
    }
}
