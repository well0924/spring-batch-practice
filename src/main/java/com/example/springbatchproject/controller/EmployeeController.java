package com.example.springbatchproject.controller;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class EmployeeController {

    private final JobLauncher jobLauncher;
    private final Job employeeBatchJob;

    @PostMapping("/batch/run")
    public String run(@RequestParam String filePath) throws Exception {
        System.out.println(">>> 컨트롤러에서 filePath=" + filePath);
        JobParameters params = new JobParametersBuilder()
                .addString("filePath", filePath)
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(employeeBatchJob, params);
        return "Batch started";
    }
}
