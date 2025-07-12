package com.example.springbatchproject.config.batch;

import com.example.springbatchproject.batch.InvalidEmployeeReportListener;
import com.example.springbatchproject.entity.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.retry.RetryException;
import org.springframework.retry.policy.MapRetryContextCache;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4); // 병렬 스레드 수
        executor.setMaxPoolSize(8);  // 최대 스레드 수
        executor.setThreadNamePrefix("batch-thread-");
        executor.initialize();
        return executor;
    }

    @Bean
    public Job employeeBatchJob(JobRepository jobRepository,
                                InvalidEmployeeReportListener listener,
                                Step employeeStep) {
        return new JobBuilder("employeeBatchJob", jobRepository)
                .start(employeeStep)
                .listener(listener)
                .build();
    }

    @Bean
    public Step employeeStep(JobRepository jobRepository,
                             PlatformTransactionManager transactionManager,
                             @Qualifier("employeeItemReader") ItemReader<Employee> employeeReader,
                             ItemProcessor<Employee, Employee> employeeProcessor,
                             ItemWriter<Employee> employeeWriter,
                             TaskExecutor taskExecutor) {
        return new StepBuilder("employeeStep", jobRepository)
                .<Employee, Employee>chunk(100, transactionManager)
                .reader(employeeReader)
                .processor(employeeProcessor)
                .writer(employeeWriter)
                //.taskExecutor(taskExecutor) // 병렬 처리
                //.faultTolerant()
                //.skip(IllegalArgumentException.class)    // 예: 상태값 Enum 오류, 데이터 타입 오류 등
                //.skipLimit(10000)                         // 최대 스킵 허용 건수 (상황 맞춰 조정)
                //.retry(RetryException.class)      // 특정 예외 시 재시도
                //.retryLimit(0)                          // 3번까지 리트라이
                //.retryContextCache(new MapRetryContextCache(100000)) // 캐시 용량 크게
                .build();
    }
}
