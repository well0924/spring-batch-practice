package com.example.springbatchproject.repository;

import com.example.springbatchproject.entity.InvalidEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidEmployeeRepository extends JpaRepository<InvalidEmployee,Long> {

}
