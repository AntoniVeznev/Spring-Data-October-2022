package com.example.exercise_7_jsonprocessing.service;

import com.example.exercise_7_jsonprocessing.module.dto.CategoriesWithProductsDto;
import com.example.exercise_7_jsonprocessing.module.dto.UserSoldDto;
import com.example.exercise_7_jsonprocessing.module.entity.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


public interface  UserService{
    void seedUsers() throws IOException;
    User findRandomUser();

    List<UserSoldDto> findAllUsersWithMoreThanOneSoldProducts();

}
