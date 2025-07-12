package com.example.springbatchproject.config.poi;

import com.example.springbatchproject.entity.Employee;
import com.example.springbatchproject.entity.consts.EmployeeStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class EmployeeExcelReader {

    public EmployeeExcelReader() {
        System.out.println(">>> EmployeeExcelReader 생성됨");
    }

    public List<Employee> readEmployeesFromExcel(String filePath) {
        List<Employee> employees = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int rowNum = 0;

            for (Row row : sheet) {
                // 첫 줄이 헤더면 건너뜀
                if (rowNum++ == 0) continue;
                // Null/Blank 체크 및 타입 변환
                String empNo = getCellValueAsString(row.getCell(0));
                String name = getCellValueAsString(row.getCell(1));
                String dept = getCellValueAsString(row.getCell(2));
                Long salary = getCellValueAsLong(row.getCell(3));
                String 입사일 = getCellValueAsString(row.getCell(4));
                String 퇴사일 = getCellValueAsString(row.getCell(5));
                String statusRaw = getCellValueAsString(row.getCell(6));
                String email =getCellValueAsString(row.createCell(7));
                String phone = getCellValueAsString(row.createCell(8));
                // 빈 값/필수값 체크 (실무 필수)
                if (empNo == null || name == null || dept == null || salary == null || statusRaw == null) {
                    // 필요시 InvalidEmployee로 분류
                    continue;
                }

                // Enum 변환(오타/대소문자/공백 방지)
                EmployeeStatus status;
                try {
                    status = EmployeeStatus.valueOf(statusRaw.trim().toUpperCase());
                } catch (Exception e) {
                    // TODO: InvalidEmployee로 분류 (상태값이 Enum에 없음)
                    continue;
                }

                // Employee 객체 생성 (엔티티 말고 DTO로 먼저 생성 추천)
                Employee emp = Employee.builder()
                        .employeeNumber(empNo)
                        .name(name)
                        .department(dept)
                        .salary(salary)
                        .status(EmployeeStatus.valueOf(statusRaw)) // Enum 변환
                        .hireDate(입사일)
                        .retireDate(퇴사일)
                        .email(email)
                        .phone(phone)
                        .build();
                employees.add(emp);
            }
        } catch (Exception e) {
            throw new RuntimeException("엑셀 파일 읽기 실패: " + e.getMessage(), e);
        }
        return employees;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                String value = cell.getStringCellValue();
                if (value == null) return null;
                value = value.trim();
                return value.isEmpty() ? null : value;

            case NUMERIC:
                // 정수/실수 구분
                double d = cell.getNumericCellValue();
                if (d == (long) d)
                    return String.valueOf((long) d);
                else
                    return String.valueOf(d);

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            case BLANK:
                return null;

            case FORMULA:
                // 수식 셀은 평가해서 String으로 리턴 (복잡하면 패스)
                try {
                    FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                    CellValue cellValue = evaluator.evaluate(cell);
                    switch (cellValue.getCellType()) {
                        case STRING: return cellValue.getStringValue().trim();
                        case NUMERIC: return String.valueOf((long) cellValue.getNumberValue());
                        case BOOLEAN: return String.valueOf(cellValue.getBooleanValue());
                        default: return null;
                    }
                } catch (Exception e) { return null; }

            default:
                return null;
        }
    }

    // 숫자 변환 (String도 안전하게 파싱)
    private Long getCellValueAsLong(Cell cell) {
        String val = getCellValueAsString(cell);
        if (isBlank(val)) return null;
        try {
            return Long.parseLong(val);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // 공백/null 체크
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
