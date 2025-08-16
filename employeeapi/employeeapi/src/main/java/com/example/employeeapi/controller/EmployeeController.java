package com.example.employeeapi.controller;

import com.example.employeeapi.entity.Employee;
import com.example.employeeapi.exception.ResourceNotFoundException;
import com.example.employeeapi.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;


    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeRepository.findById(id)
                .map(ResponseEntity::ok) // 200 OK
                .orElseThrow(() -> new ResourceNotFoundException("Employee with ID "+id+ " not found")); // 404 Not Found
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee savedEmployee = employeeRepository.save(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
        Optional<Employee> existingEmployee= employeeRepository.findById(id);

        if(existingEmployee.isPresent()){
            Employee employee = existingEmployee.get();
            employee.setName(updatedEmployee.getName());
            employee.setEmail(updatedEmployee.getEmail());
            employee.setDepartment(updatedEmployee.getDepartment());
            Employee savedEmployee = employeeRepository.save(employee);
            return ResponseEntity.ok(savedEmployee);
        }else{
            throw new ResourceNotFoundException("Employee with ID "+id+ " not found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable Long id){
        Optional<Employee> existingEmployee=employeeRepository.findById(id);
        if(existingEmployee.isPresent()){
            employeeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }else{
            throw new ResourceNotFoundException("Employee with ID "+id+ " not found");
        }
    }
}
