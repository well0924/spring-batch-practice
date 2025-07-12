package com.example.springbatchproject;

import com.example.springbatchproject.config.poi.EmployeeExcelPoiWriter;
import com.example.springbatchproject.config.poi.EmployeeExcelReader;
import com.example.springbatchproject.entity.Employee;
import com.example.springbatchproject.entity.consts.EmployeeStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@EnableBatchProcessing
@SpringBootApplication
public class SpringBatchProjectApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(SpringBatchProjectApplication.class, args);

        List<Employee> employeeList = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Random rnd = new Random();


        for (int i = 1; i <= 100_000; i++) {

            // 입사일: 2020-01-01 ~ 2023-12-31 랜덤 날짜
            LocalDate hire = LocalDate.of(2020, 1, 1).plusDays(rnd.nextInt(1460));
            String hireDate = hire.format(fmt);

            String retireDate = null;
            // 10명 중 2명은 퇴사 처리 (입사일 + 90~600일 뒤)
            if (i % 10 < 2) {
                retireDate = hire.plusDays(90 + rnd.nextInt(510)).format(fmt);
            }


            employeeList.add(
                    Employee.builder()
                            .employeeNumber(String.format("%05d", i))
                            .name("직원" + i)
                            .department("부서" + (i % 10))
                            .salary(3_000_000L + (i % 500_000))
                            .hireDate(hireDate)
                            .retireDate(retireDate)
                            .status(EmployeeStatus.ACTIVE)
                            .build()
            );
        }
        String baseDir = "C:/spring_work";
        File dir = new File(baseDir);
        if (!dir.exists()) dir.mkdirs();

        String output = baseDir + "/bulk_employees_" + System.currentTimeMillis() + ".xlsx";
        new EmployeeExcelPoiWriter().writeEmployeesToExcel(employeeList, output);

        JobLauncher jobLauncher = context.getBean(JobLauncher.class);
        Job employeeBatchJob = context.getBean("employeeBatchJob", Job.class);

        JobParameters params = new JobParametersBuilder()
                .addString("filePath", output) // 파일 경로 하드코딩
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        try {
            jobLauncher.run(employeeBatchJob, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
