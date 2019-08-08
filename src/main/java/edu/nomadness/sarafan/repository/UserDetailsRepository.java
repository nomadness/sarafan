package edu.nomadness.sarafan.repository;

import edu.nomadness.sarafan.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<User, String> {
}
