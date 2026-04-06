package com.to4ilochka.bookspace.service.impl;

import com.to4ilochka.bookspace.dto.employee.CreateEmployeeRequest;
import com.to4ilochka.bookspace.dto.employee.EmployeeResponse;
import com.to4ilochka.bookspace.dto.employee.UpdateEmployeeRequest;
import com.to4ilochka.bookspace.mapper.EmployeeMapper;
import com.to4ilochka.bookspace.model.Employee;
import com.to4ilochka.bookspace.model.enums.Role;
import com.to4ilochka.bookspace.repo.EmployeeRepository;
import com.to4ilochka.bookspace.repo.UserRepository;
import com.to4ilochka.bookspace.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public EmployeeResponse getMyProfile(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .map(employeeMapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeMapper.toResponseList(employeeRepository.findAll());
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
    }

    @Transactional
    @Override
    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Employee employee = employeeMapper.toEntity(request);
        employee.getUser().setPassword(passwordEncoder.encode(request.password()));
        employee.getUser().getRoles().add(Role.ROLE_EMPLOYEE);

        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Transactional
    @Override
    public EmployeeResponse updateEmployee(Long id, UpdateEmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        employee.getUser().setName(request.name());
        employee.setPhone(request.phone());

        return employeeMapper.toResponse(employeeRepository.save(employee));
    }
}