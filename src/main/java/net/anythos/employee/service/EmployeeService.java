package net.anythos.employee.service;

import lombok.RequiredArgsConstructor;
import net.anythos.employee.entity.Employee;
import net.anythos.employee.entity.EmployeeDto;
import net.anythos.employee.repository.EmployeeRepository;
import net.anythos.user.entity.Role;
import net.anythos.user.entity.User;
import net.anythos.user.entity.UserDto;
import net.anythos.user.repository.RoleRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static net.anythos.employee.entity.EmplloyeeMapper.mapToEmployee;
import static net.anythos.employee.entity.EmployeeDtoMapper.mapToEmployeeDto;
import static net.anythos.employee.entity.EmployeeDtoMapper.mapToEmployeeDtoWithUser;
import static net.anythos.user.entity.UserDtoMapper.mapToUserDto;
import static net.anythos.user.entity.UserMapper.mapToUser;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class EmployeeService {
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;

    private final Logger logger = LogManager.getLogger(getClass());

    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        UserDto userDto = employeeDto.getUserDto();
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setRoles(roleRepository.findByNameIn(userDto.getRoles().stream().map(Role::getName).collect(Collectors.toSet())));
        User user = mapToUser(userDto);
        Employee employee = mapToEmployee(employeeDto);
        employee.setUser(user);
        employee = employeeRepository.save(employee);

        employeeDto = mapToEmployeeDto(employee);
        employeeDto.setUserDto(mapToUserDto(employee.getUser()));
        return employeeDto;
    }

    public EmployeeDto getEmplyeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found employee by id: " + id));
        User user = employee.getUser();
        logger.info("found");
        EmployeeDto employeeDto = mapToEmployeeDto(employee);
        employeeDto.setUserDto(mapToUserDto(user));
        return employeeDto;
    }

    public List<EmployeeDto> getEmployee() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map(employee -> mapToEmployeeDtoWithUser(employee, employee.getUser())).toList(); //.setUserDto(mapToUserDto(employee.getUser()))
    }
}
