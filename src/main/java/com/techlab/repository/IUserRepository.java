package com.techlab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.techlab.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("userRepository")
public interface IUserRepository extends JpaRepository<User, Long> {

    boolean existsByName(String name);

    Optional<User> findByName(String name);

    List<User> findByActiveTrue();

}

