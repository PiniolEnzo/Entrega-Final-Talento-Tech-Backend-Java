package com.techlab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.techlab.entity.Category;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("categoryRepository")
public interface ICategoryRepository extends JpaRepository<Category, Short> {

    Optional<Category> findByName(String name);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Short id);
}

