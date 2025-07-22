package com.techlab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.techlab.entity.Category;
import org.springframework.stereotype.Repository;

@Repository("categoryRepository")
public interface ICategoryRepository extends JpaRepository<Category, Short> {

}

