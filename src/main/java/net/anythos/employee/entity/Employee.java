package net.anythos.employee.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.anythos.user.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

@Table(name = "employees")
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    private Integer employeeId;

    private String firstName;

    private String lastName;

    private String email;

    private String businessMobile;

    private String department;

    private String profession; //function reserved word for mysql

    private String grade;

    private LocalDate hireDate;

    private Double fte;

    private BigDecimal salary;

    @Basic
    private Integer contractTypeValue;

    @Transient
    private ContractType contractType;

    @PostLoad
    void fillTransient() {
        if (contractTypeValue > 0) {
            this.contractType = ContractType.of(contractTypeValue);
        }
    }

    @PrePersist
    void fillPersistent() {
        if (contractType != null) {
            this.contractTypeValue = contractType.getType();
        }
    }

    public enum ContractType {
        EMPLOYMENT(1),
        COMMISSION(2),
        SPECIFIC_TASK(3);

        private int type;

        ContractType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public static ContractType of(int type) {
            return Stream.of(ContractType.values())
                    .filter(t -> t.getType() == type)
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
    }
}
