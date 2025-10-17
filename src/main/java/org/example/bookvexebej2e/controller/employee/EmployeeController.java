package org.example.bookvexebej2e.controller.employee;

import org.example.bookvexebej2e.dto.employee.*;
import org.example.bookvexebej2e.service.employee.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/employees")
public class EmployeeController {
    
    private final EmployeeService employeeService;
    
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> findAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }
    
    @GetMapping("/pagination")
    public ResponseEntity<Page<EmployeeResponse>> findAll(EmployeeQuery query) {
        return ResponseEntity.ok(employeeService.findAll(query));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@RequestBody EmployeeCreate createDto) {
        return ResponseEntity.ok(employeeService.create(createDto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(@PathVariable UUID id, @RequestBody EmployeeUpdate updateDto) {
        return ResponseEntity.ok(employeeService.update(id, updateDto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        employeeService.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        employeeService.activate(id);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        employeeService.deactivate(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/select")
    public ResponseEntity<List<EmployeeSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(employeeService.findAllForSelect());
    }
}
