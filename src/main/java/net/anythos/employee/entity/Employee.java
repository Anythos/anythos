package net.anythos.employee.entity;


import lombok.*;
import net.anythos.user.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "employees")
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(mappedBy="employee", cascade = CascadeType.ALL)
    private User user;

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

    @Enumerated(EnumType.STRING)
    private ContractType contractType;

    public void setUser(User user) {
        if (user == null) {
            if (this.user != null) {
                this.user.setEmployee(null);
            }
        }
        else {
            user.setEmployee(this);
        }
        this.user = user;
    }



}
