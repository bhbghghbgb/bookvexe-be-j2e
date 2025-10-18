package org.example.bookvexebej2e.controllers.employee;

import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.employee.EmployeeCreate;
import org.example.bookvexebej2e.models.dto.employee.EmployeeQuery;
import org.example.bookvexebej2e.models.dto.employee.EmployeeResponse;
import org.example.bookvexebej2e.models.dto.employee.EmployeeSelectResponse;
import org.example.bookvexebej2e.models.dto.employee.EmployeeUpdate;
import org.example.bookvexebej2e.services.employee.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/pagination")
    public ResponseEntity<Page<EmployeeResponse>> findAll2(@RequestBody EmployeeQuery query) {
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
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/activate/{id}")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        employeeService.activate(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        employeeService.deactivate(id);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/select")
    public ResponseEntity<List<EmployeeSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(employeeService.findAllForSelect());
    }

    @GetMapping("/select/pagination")
    public ResponseEntity<Page<EmployeeSelectResponse>> findAllForSelect(EmployeeQuery query) {
        return ResponseEntity.ok(employeeService.findAllForSelect(query));
    }

    @PostMapping("/select/pagination")
    public ResponseEntity<Page<EmployeeSelectResponse>> findAllForSelect2(@RequestBody EmployeeQuery query) {
        return ResponseEntity.ok(employeeService.findAllForSelect(query));
    }

}
