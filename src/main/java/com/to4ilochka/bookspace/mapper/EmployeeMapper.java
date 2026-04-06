package com.to4ilochka.bookspace.mapper;

import com.to4ilochka.bookspace.dto.employee.CreateEmployeeRequest;
import com.to4ilochka.bookspace.dto.employee.EmployeeResponse;
import com.to4ilochka.bookspace.model.Employee;
import com.to4ilochka.bookspace.model.enums.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", imports = {Set.class, Role.class})
public interface EmployeeMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "name", source = "user.name")
    EmployeeResponse toResponse(Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.email", source = "email")
    @Mapping(target = "user.name", source = "name")
    @Mapping(target = "user.password", ignore = true)
    @Mapping(target = "user.roles", expression = "java(Set.of(Role.ROLE_EMPLOYEE))")
    Employee toEntity(CreateEmployeeRequest createEmployeeRequest);

    List<EmployeeResponse> toResponseList(List<Employee> employees);
}