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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void getMyProfile_Success() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        EmployeeResponse expectedResponse = mock(EmployeeResponse.class);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employeeMapper.toResponse(employee)).thenReturn(expectedResponse);

        EmployeeResponse result = employeeService.getMyProfile(employeeId);

        assertEquals(expectedResponse, result);
    }

    @Test
    void getMyProfile_NotFound() {
        Long employeeId = 1L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getMyProfile(employeeId));
    }

    @Test
    void getAllEmployees_Success() {
        List<Employee> employees = List.of(new Employee(), new Employee());
        List<EmployeeResponse> expectedResponse = List.of(mock(EmployeeResponse.class), mock(EmployeeResponse.class));

        when(employeeRepository.findAll()).thenReturn(employees);
        when(employeeMapper.toResponseList(employees)).thenReturn(expectedResponse);

        List<EmployeeResponse> result = employeeService.getAllEmployees();

        assertEquals(expectedResponse, result);
        assertEquals(2, result.size());
    }

    @Test
    void getEmployeeById_Success() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        EmployeeResponse expectedResponse = mock(EmployeeResponse.class);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employeeMapper.toResponse(employee)).thenReturn(expectedResponse);

        EmployeeResponse result = employeeService.getEmployeeById(employeeId);

        assertEquals(expectedResponse, result);
    }

    @Test
    void getEmployeeById_NotFound() {
        Long employeeId = 1L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById(employeeId));
    }

    @Test
    void createEmployee_Success() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@mail.com");
        user.setRoles(new HashSet<>(List.of(Role.ROLE_CLIENT)));

        CreateEmployeeRequest request = new CreateEmployeeRequest(
                "user@mail.com", "Name", "123456", LocalDate.of(1990, 1, 1)
        );
        EmployeeResponse expectedResponse = mock(EmployeeResponse.class);

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(clientRepository.existsById(user.getId())).thenReturn(true);
        when(employeeRepository.existsById(user.getId())).thenReturn(false);
        doNothing().when(employeeMapper).updateEntityFromRequest(any(CreateEmployeeRequest.class), any(Employee.class));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(i -> i.getArguments()[0]);
        when(employeeMapper.toResponse(any(Employee.class))).thenReturn(expectedResponse);

        EmployeeResponse result = employeeService.createEmployee(request);

        assertTrue(user.getRoles().contains(Role.ROLE_EMPLOYEE));
        assertEquals(expectedResponse, result);
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void createEmployee_UserNotFound() {
        CreateEmployeeRequest request = new CreateEmployeeRequest(
                "user@mail.com", "Name", "123456", LocalDate.of(1990, 1, 1)
        );

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.createEmployee(request));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void createEmployee_ClientNotRegistered() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@mail.com");

        CreateEmployeeRequest request = new CreateEmployeeRequest(
                "user@mail.com", "Name", "123456", LocalDate.of(1990, 1, 1)
        );

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(clientRepository.existsById(user.getId())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> employeeService.createEmployee(request));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void createEmployee_AlreadyEmployee() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@mail.com");

        CreateEmployeeRequest request = new CreateEmployeeRequest(
                "user@mail.com", "Name", "123456", LocalDate.of(1990, 1, 1)
        );

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(clientRepository.existsById(user.getId())).thenReturn(true);
        when(employeeRepository.existsById(user.getId())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> employeeService.createEmployee(request));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void updateEmployee_Success() {
        Long employeeId = 1L;
        UpdateEmployeeRequest request = mock(UpdateEmployeeRequest.class);
        when(request.name()).thenReturn("New Name");
        when(request.phone()).thenReturn("987654321");

        User user = new User();
        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setUser(user);
        EmployeeResponse expectedResponse = mock(EmployeeResponse.class);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.toResponse(employee)).thenReturn(expectedResponse);

        EmployeeResponse result = employeeService.updateEmployee(employeeId, request);

        assertEquals("New Name", employee.getUser().getName());
        assertEquals("987654321", employee.getPhone());
        assertEquals(expectedResponse, result);
        verify(employeeRepository).save(employee);
    }

    @Test
    void updateEmployee_NotFound() {
        Long employeeId = 1L;
        UpdateEmployeeRequest request = mock(UpdateEmployeeRequest.class);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.updateEmployee(employeeId, request));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void fireEmployee_Success() {
        Long employeeId = 1L;
        User user = new User();
        user.setId(employeeId);
        user.setRoles(new HashSet<>(List.of(Role.ROLE_CLIENT, Role.ROLE_EMPLOYEE)));

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setUser(user);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        employeeService.fireEmployee(employeeId);

        assertFalse(user.getRoles().contains(Role.ROLE_EMPLOYEE));
        verify(userRepository).save(user);
        verify(employeeRepository).deleteEmployeeById(employeeId);
    }

    @Test
    void fireEmployee_NotFound() {
        Long employeeId = 1L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.fireEmployee(employeeId));
        verify(employeeRepository, never()).deleteEmployeeById(any());
    }
}