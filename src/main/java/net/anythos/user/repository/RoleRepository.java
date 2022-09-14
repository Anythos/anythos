package net.anythos.user.repository;

import net.anythos.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	Optional<Role> findByName(String name);
	Role findRoleByName(String name);
	Set<Role> findByNameIn(Set<String> rolesNames);
}
