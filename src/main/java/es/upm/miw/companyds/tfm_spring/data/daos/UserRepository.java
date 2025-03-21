package es.upm.miw.companyds.tfm_spring.data.daos;

import es.upm.miw.companyds.tfm_spring.data.model.Role;
import es.upm.miw.companyds.tfm_spring.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByMobile(String mobile);
    List<User> findByRoleIn(Collection<Role> roles);
}
