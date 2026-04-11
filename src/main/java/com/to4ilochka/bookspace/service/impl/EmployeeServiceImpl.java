package com.to4ilochka.bookspace.service.impl;

import com.to4ilochka.bookspace.dto.employee.CreateEmployeeRequest;
import com.to4ilochka.bookspace.dto.employee.EmployeeResponse;
import com.to4ilochka.bookspace.dto.employee.UpdateEmployeeRequest;
import com.to4ilochka.bookspace.exception.ResourceAlreadyExistsException;
import com.to4ilochka.bookspace.exception.ResourceNotFoundException;
import com.to4ilochka.bookspace.mapper.EmployeeMapper;
import com.to4ilochka.bookspace.model.Employee;
import com.to4ilochka.bookspace.model.User;
import com.to4ilochka.bookspace.model.enums.Role;
import com.to4ilochka.bookspace.repo.ClientRepository;
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
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeResponse getMyProfile(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .map(employeeMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
    }

    @Transactional
    @Override
    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User with this email not found"));

        if (!clientRepository.existsById(user.getId())) {
            throw new ResourceNotFoundException("Client must be registered as a client first");
        }

        if (employeeRepository.existsById(user.getId())) {
            throw new ResourceAlreadyExistsException("Client is already an employee");
        }

        Employee employee = new Employee();
        employee.setUser(user);

        employeeMapper.updateEntityFromRequest(request, employee);

        user.getRoles().add(Role.ROLE_EMPLOYEE);

        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Transactional
    @Override
    public EmployeeResponse updateEmployee(Long id, UpdateEmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        employee.getUser().setName(request.name());
        employee.setPhone(request.phone());

        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Transactional
    @Override
    public void fireEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        User user = employee.getUser();
        user.getRoles().remove(Role.ROLE_EMPLOYEE);
        userRepository.save(user);

        employeeRepository.deleteEmployeeById(employeeId);
    }
}