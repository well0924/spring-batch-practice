package com.example.springbatchproject.batch;

import com.example.springbatchproject.entity.Employee;
import com.example.springbatchproject.entity.InvalidEmployee;
import com.example.springbatchproject.repository.InvalidEmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmployeeProcessor implements ItemProcessor<Employee, Employee> {

    private final InvalidEmployeeRepository invalidEmployeeRepository; // 실패 데이터 저장
    private static final Set<String> duplicateCheckSet = new HashSet<>();

    @Override
    public Employee process(Employee item) throws Exception {

        // 1. 중복 사번 체크 (메모리 기반, 실무는 DB에 쿼리도 가능)
//        if (!duplicateCheckSet.add(item.getEmployeeNumber())) {
//            invalidEmployeeRepository.save(InvalidEmployee.of(item, "중복 사번"));
//            return null; // Writer로 전달 안 됨
//        }
        // 2. 급여 음수 체크
//        if (item.getSalary() == null || item.getSalary() < 0) {
//            invalidEmployeeRepository.save(InvalidEmployee.of(item, "급여값 비정상"));
//            return null;
//        }
        // 3. 상태값 Enum 유효성 (이미 Reader에서 변환했으니 통과)
        // 기타 추가 비즈니스 로직 가능
        log.info("Processor 통과: {}", item.getEmployeeNumber());
        return item; // 정상 데이터만 Writer로 전달
    }
}
