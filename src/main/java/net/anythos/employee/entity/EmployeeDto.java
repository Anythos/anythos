package net.anythos.employee.entity;

import lombok.*;
import net.anythos.user.entity.UserDto;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmployeeDto {

    private Long id;

    private UserDto userDto;

    private Integer systemEmployeeId;

    private String firstName;

    private String lastName;

    private String email;

    private String businessMobile;

    private String department;

    private String profession; //function - reserved word for mysql

    private String grade;

    private LocalDate hireDate;

    private Double fte;

    private BigDecimal salary;

    private ContractType contractType;
}
