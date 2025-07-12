package com.example.springbatchproject.entity;

import com.example.springbatchproject.entity.consts.EmployeeStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "employee")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String employeeNumber; // 사번

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private Long salary;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployeeStatus status; // 'ACTIVE', 'RETIRED', 등

    private String position;

    private String email;

    private String phone;

    private String hireDate;   // 입사일 (yyyy-MM-dd)
    private String retireDate; // 퇴사일 (yyyy-MM-dd)

}
