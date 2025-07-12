package com.example.springbatchproject.config.batch;

import com.example.springbatchproject.batch.EmployeeReader;
import com.example.springbatchproject.config.poi.EmployeeExcelReader;
import com.example.springbatchproject.entity.Employee;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.List;

@Configuration
public class BatchReaderConfig {

    @Bean(name = "employeeItemReader")
    @StepScope
    public ItemReader<Employee> employeeItemReader(
            @Value("#{jobParameters['filePath']}") String filePath,
            EmployeeExcelReader excelReader) {
        System.out.println(">>> employeeItemReader() filePath=" + filePath);  // <<<<<
        List<Employee> employeeList = excelReader.readEmployeesFromExcel(filePath);
        System.out.println(">>> 읽은 직원 수=" + employeeList.size());
        return new EmployeeReader(employeeList);
    }
}
