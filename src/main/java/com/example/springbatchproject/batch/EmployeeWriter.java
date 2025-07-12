package com.example.springbatchproject.batch;

import com.example.springbatchproject.entity.Employee;
import com.example.springbatchproject.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmployeeWriter implements ItemWriter<Employee> {

    private final EmployeeRepository employeeRepository;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void write(Chunk<? extends Employee> chunk) throws Exception {
        String sql = "INSERT INTO batchs_practice.employee " +
                "(employee_number, name, department, salary, status,hire_date,retire_date,email,phone) " +
                "VALUES ( ?, ?, ?, ?, ?, ?, ?,?,?)";
        try {
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Employee emp = chunk.getItems().get(i);
                    ps.setString(1, emp.getEmployeeNumber());
                    ps.setString(2, emp.getName());
                    ps.setString(3, emp.getDepartment());
                    ps.setLong(4, emp.getSalary());
                    ps.setString(5, emp.getStatus().name());
                    ps.setString(6, emp.getHireDate());
                    ps.setString(7, emp.getRetireDate());
                    ps.setString(8, emp.getEmail());
                    ps.setString(9, emp.getPhone());
                }
                @Override
                public int getBatchSize() {
                    return chunk.getItems().size();
                }
            });
            log.info("Writer 들어온 데이터 수: {}", chunk.size());
        }  catch (DuplicateKeyException e) {
            log.error("Duplicate key error: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Insert error: ", e);
        }
    }

//    @Override
//    public void write(Chunk<? extends Employee> chunk) throws Exception {
//        employeeRepository.saveAll(chunk.getItems());
//        //실패/에러 데이터는 Processor에서 이미 분리해둠
//        chunk.forEach(e -> log.info("Writer 데이터: {}", e.getId()));
//        log.info("Writer: 데이터 수={}", chunk.size());
//
//
//    }


}
