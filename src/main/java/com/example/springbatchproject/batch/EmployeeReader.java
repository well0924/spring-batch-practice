package com.example.springbatchproject.batch;

import com.example.springbatchproject.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class EmployeeReader  implements ItemReader<Employee> {

    private final Iterator<Employee> employeeIterator;

    public EmployeeReader (List<Employee> employeeList) {
        this.employeeIterator = employeeList.iterator();
    }

    @Override
    public Employee read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        Employee emp = employeeIterator.hasNext() ? employeeIterator.next() : null;
        if (emp != null) {
            log.info("Reader에서 읽은 Employee: " + emp.getEmployeeNumber());
        } else {
            log.info("Reader에서 읽은 Employee: null");
        }
        return emp;
    }
}
