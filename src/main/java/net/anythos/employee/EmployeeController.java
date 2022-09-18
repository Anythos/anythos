package net.anythos.employee;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.anythos.employee.entity.EmployeeDto;
import net.anythos.employee.service.EmployeeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/anythos")
public class EmployeeController {

    private final Logger logger = LogManager.getLogger(getClass());
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/admin/employees")
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) {
        employeeDto = employeeService.createEmployee(employeeDto);
        URI uri = URI.create("/admin/employee/" + employeeDto.getId());
        return ResponseEntity.created(uri).body(employeeDto);
    }

    @GetMapping("/admin/employees")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeDto> getEmployee() {
        return employeeService.getEmployees();
    }

    @DeleteMapping("/admin/employees/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployeeById(@PathVariable long id){
        employeeService.deleteEmployee( id);
    }

    @GetMapping("/admin/employees/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDto getEmployeeById(@PathVariable long id) {
        return employeeService.getEmplyeeById(id);
    }

    @PutMapping("/admin/employee/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDto updateEmployee(@PathVariable long id, @RequestBody EmployeeDto employeeDto)
    {
        return employeeService.update(id, employeeDto);
    }


}
