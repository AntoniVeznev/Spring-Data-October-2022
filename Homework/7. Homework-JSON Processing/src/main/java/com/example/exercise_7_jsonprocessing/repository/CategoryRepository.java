package com.example.exercise_7_jsonprocessing.repository;


import com.example.exercise_7_jsonprocessing.module.dto.CategoriesWithProductsDto;
import com.example.exercise_7_jsonprocessing.module.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c " +
            "from Category c  order by size(c.products) desc ")
    List<Category> findLetsTest();

}
