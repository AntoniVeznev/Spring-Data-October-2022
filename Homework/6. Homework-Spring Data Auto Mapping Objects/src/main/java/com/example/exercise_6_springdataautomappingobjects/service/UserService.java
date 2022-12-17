package com.example.exercise_6_springdataautomappingobjects.service;

import com.example.exercise_6_springdataautomappingobjects.model.dto.UserLoginDto;
import com.example.exercise_6_springdataautomappingobjects.model.dto.UserRegisterDto;

public interface UserService {
    void registerUser(UserRegisterDto userRegisterDto);

    void loginUser(UserLoginDto userLoginDto);

    void logoutUser();
}