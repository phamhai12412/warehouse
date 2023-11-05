package ra.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.RoleName;
import ra.model.Roles;


import java.util.Optional;
@Repository

public interface IRoleRepository extends JpaRepository<Roles,Long> {

    Optional<Roles> findByRoleName(RoleName roleName);
}
