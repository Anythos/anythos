package net.anythos.employee.entity;

public class EmplloyeeMapper {
    public static Employee mapToEmployee(EmployeeDto employeeDto) {
        Employee employee = Employee.builder()
                .id(employeeDto.getId())
                .systemEmployeeId(employeeDto.getSystemEmployeeId())
                .firstName(employeeDto.getFirstName())
                .lastName(employeeDto.getLastName())
                .email(employeeDto.getEmail())
                .businessMobile(employeeDto.getBusinessMobile())
                .department(employeeDto.getDepartment())
                .profession(employeeDto.getProfession())
                .grade(employeeDto.getGrade())
                .hireDate(employeeDto.getHireDate())
                .fte(employeeDto.getFte())
                .salary(employeeDto.getSalary())
                .contractType(employeeDto.getContractType())
                .build();
        return employee;
    }
}
