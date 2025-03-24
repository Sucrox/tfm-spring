package es.upm.miw.companyds.tfm_spring.persistence.repository;

import es.upm.miw.companyds.tfm_spring.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByPhone(String mobile);
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}
