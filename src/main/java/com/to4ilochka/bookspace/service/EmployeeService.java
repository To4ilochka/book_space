package com.to4ilochka.bookspace.service;

import com.to4ilochka.bookspace.dto.employee.EmployeeDTO;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> getAllEmployees();
    EmployeeDTO getEmployeeByEmail(String email);
    EmployeeDTO updateEmployeeByEmail(String email, EmployeeDTO employee);
    void deleteEmployeeByEmail(String email);
    EmployeeDTO addEmployee(EmployeeDTO employee);
}
