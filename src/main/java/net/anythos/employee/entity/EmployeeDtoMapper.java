package net.anythos.employee.entity;

import net.anythos.user.entity.User;

import static net.anythos.user.entity.UserDtoMapper.mapToUserDto;

public class EmployeeDtoMapper {

    public static EmployeeDto mapToEmployeeDto(Employee employee) {
        return createEmployeeDtoBuilder(employee).build();
    }

    public static EmployeeDto mapToEmployeeDtoWithUser(Employee employee, User user) {
        return createEmployeeDtoBuilder(employee)
                .userDto(mapToUserDto(user))
                .build();
    }

    private static EmployeeDto.EmployeeDtoBuilder createEmployeeDtoBuilder(Employee employee) {
        return EmployeeDto.builder()
                .id(employee.getId())
                .systemEmployeeId(employee.getSystemEmployeeId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .businessMobile(employee.getBusinessMobile())
                .department(employee.getDepartment())
                .profession(employee.getProfession())
                .grade(employee.getGrade())
                .hireDate(employee.getHireDate())
                .fte(employee.getFte())
                .salary(employee.getSalary())
                .contractType(employee.getContractType());
    }

}
