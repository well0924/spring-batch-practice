package com.example.springbatchproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "invalid_employee")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvalidEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeNumber;
    private String name;
    private String department;
    private Long salary;
    private String status;
    private String position;
    private String email;
    private String phone;
    private String hireDate;
    private String retireDate;

    @Lob
    private String errorReason; // 오류 사유 (음수 급여, 중복, 포맷불일치 등)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_log_id")
    private BatchProcessLog batchProcessLog; // 어떤 배치에서 발생했는지


    public static InvalidEmployee of(Employee emp, String reason) {
        return InvalidEmployee.builder()
                .employeeNumber(emp.getEmployeeNumber())
                .name(emp.getName())
                .department(emp.getDepartment())
                .salary(emp.getSalary())
                .status(emp.getStatus() != null ? emp.getStatus().name() : null)
                .errorReason(reason)
                .createdAt(LocalDateTime.now())
                .build();
    }

}
