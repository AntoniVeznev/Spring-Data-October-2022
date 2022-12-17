package com.example.exercise_6_springdataautomappingobjects.service.impl;

import com.example.exercise_6_springdataautomappingobjects.model.dto.UserLoginDto;
import com.example.exercise_6_springdataautomappingobjects.model.dto.UserRegisterDto;
import com.example.exercise_6_springdataautomappingobjects.model.entity.User;
import com.example.exercise_6_springdataautomappingobjects.repository.UserRepository;
import com.example.exercise_6_springdataautomappingobjects.service.UserService;
import com.example.exercise_6_springdataautomappingobjects.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    protected User loggedInUser;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void registerUser(UserRegisterDto userRegisterDto) {
        if (!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword())) {
            System.out.println("Wrong confirm password!!!!!!!!!!!!!!!!!!!!!!");
            return;
        }
        Set<ConstraintViolation<UserRegisterDto>> violations =
                validationUtil.getViolations(userRegisterDto);
        if (!violations.isEmpty()) {
            violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            return;
        }
        User user = modelMapper.map(userRegisterDto, User.class);

        if (userRepository.count() == 0) {
            user.setAdmin(true);
        }

        userRepository.save(user);
        System.out.printf("%s was registered.%n", user.getFullName());
    }

    @Override
    public void loginUser(UserLoginDto userLoginDto) {
        Set<ConstraintViolation<UserLoginDto>> violations =
                validationUtil.getViolations(userLoginDto);
        if (!violations.isEmpty()) {
            violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            return;
        }
        User user = userRepository
                .findByEmailAndPassword(userLoginDto.getEmail(), userLoginDto.getPassword())
                .orElse(null);
        if (user == null) {
            System.out.println("No such user in database!!!!!!!!!!!!!!!!!!!!");
            return;
        } else {
            System.out.printf("Successfully logged in %s.%n", user.getFullName());
        }
        loggedInUser = user;
    }

    @Override
    public void logoutUser() {
        if (loggedInUser == null) {
            System.out.println("Cannot log out. No user was logged in!!!!!!!!!!!!!!!!");
        } else {
            System.out.printf("User %s successfully logged out.%n", loggedInUser.getFullName());
            loggedInUser = null;
        }
    }
}