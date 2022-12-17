package com.example.exercise_4_springdataintro.service;

import com.example.exercise_4_springdataintro.model.entity.Category;

import java.io.IOException;
import java.util.Set;

public interface CategoryService {

    void seedCategories() throws IOException;

    Set<Category> getRandomCategory();
}
