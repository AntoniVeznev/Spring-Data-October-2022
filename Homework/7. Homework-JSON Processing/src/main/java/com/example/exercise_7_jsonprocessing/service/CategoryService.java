package com.example.exercise_7_jsonprocessing.service;


import com.example.exercise_7_jsonprocessing.module.dto.CategoriesWithProductsDto;
import com.example.exercise_7_jsonprocessing.module.entity.Category;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface CategoryService {

    void seedCategories() throws IOException;

    Set<Category> findRandomCategories();


    List<CategoriesWithProductsDto> findTEST();

}
