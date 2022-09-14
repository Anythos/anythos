package net.anythos.user.entity;

import lombok.*;
import net.anythos.employee.entity.EmployeeDto;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private boolean active;
    private Set<Role> roles = new HashSet<>();
}
