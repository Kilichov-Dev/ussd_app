package ecma.ai.ussdapp.repository;

import ecma.ai.ussdapp.entity.Role;
import ecma.ai.ussdapp.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(RoleName roleName);
}
