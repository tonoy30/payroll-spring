package com.tonoy.payroll.employee;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    private final EmployeeRepository employeeRepository;

    EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // get all the employee
    @GetMapping
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    // create a new employee
    @PostMapping
    public Employee createEmployee(@RequestBody Employee newEmployee) {
        return employeeRepository.save(newEmployee);
    }

    // get single employee by ID
    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable Long id) {
        return employeeRepository
                .findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    // update employee
    @PutMapping("/{id}")
    public Employee updateEmployee(@RequestBody Employee updatedEmployee, @PathVariable Long id) {
        return employeeRepository
                .findById(id)
                .map(employee -> {
                    employee.setName(updatedEmployee.getName());
                    employee.setRole(updatedEmployee.getRole());
                    return employeeRepository.save(employee);
                })
                .orElseGet(() -> {
                    updatedEmployee.setId(id);
                    return employeeRepository.save(updatedEmployee);
                });
    }

    // delete employee
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        try {
            employeeRepository.deleteById(id);
        } catch (Exception e) {
            throw new EmployeeNotFoundException(id);
        }
    }
}
