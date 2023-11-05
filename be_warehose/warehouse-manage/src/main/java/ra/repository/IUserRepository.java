package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.model.RoleName;
import ra.model.Roles;
import ra.model.StatusName;
import ra.model.Users;


import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<Users, Long> {
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<Users> findByEmail(String email);

    Page<Users> findAllByEmailContainingIgnoreCase(String email, Pageable pageable);
    @Query("SELECT count (u) FROM Users u WHERE u.status = :status")
    Integer totalUsers(@Param("status") boolean status);
    @Query("SELECT u FROM Users u WHERE u.roles.roleName = :roles")
    List<Users> listUsers(@Param("roles") RoleName roles);

}