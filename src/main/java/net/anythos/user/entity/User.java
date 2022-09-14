package net.anythos.user.entity;

import lombok.*;
import net.anythos.employee.entity.Employee;
import net.anythos.security.refreshtoken.RefreshToken;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Table(name = "users")
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @Id
    private Long id;

    @NotEmpty
    @Column(unique = true, nullable = false)
    private String username;

    @NotEmpty
    @Column(nullable = false)
    private String password;

    @NotNull
    private boolean active;

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private RefreshToken refreshToken;


    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Employee employee;

    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        roles.add(role);
    }

//    public void setEmployee(Employee employee){
//        if(employee==null){
//            if(this.employee!=null){
//                this.employee.setUser(null);
//            }
//        }
//        else{
//            employee.setUser(this);
//        }
//        this.employee = employee;
//    }
}