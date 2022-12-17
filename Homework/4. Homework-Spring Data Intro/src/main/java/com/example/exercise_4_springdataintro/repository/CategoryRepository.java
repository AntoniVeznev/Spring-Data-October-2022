package com.example.exercise_4_springdataintro.repository;

import com.example.exercise_4_springdataintro.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
