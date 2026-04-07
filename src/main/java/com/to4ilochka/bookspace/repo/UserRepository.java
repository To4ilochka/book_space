package com.to4ilochka.bookspace.repo;

import com.to4ilochka.bookspace.model.User;
import com.to4ilochka.bookspace.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByEmailIn(Collection<String> emails);

    Optional<User> findByEmail(String email);

    boolean existsByRoles(Role role);

    boolean existsByEmail(String email);
}