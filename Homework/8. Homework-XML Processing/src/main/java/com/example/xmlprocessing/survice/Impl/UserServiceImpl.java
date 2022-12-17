package com.example.xmlprocessing.survice.Impl;

import com.example.xmlprocessing.model.dto.UserSeedDto;
import com.example.xmlprocessing.model.dto.UserSeedRootDto;
import com.example.xmlprocessing.model.dto.UserWithProductsDto;
import com.example.xmlprocessing.model.dto.UsersViewRootDto;
import com.example.xmlprocessing.model.entity.User;
import com.example.xmlprocessing.repository.UserRepository;
import com.example.xmlprocessing.survice.UserService;
import com.example.xmlprocessing.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;


    public UserServiceImpl(UserRepository userRepository, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;

    }

    @Override
    public Long getCount() {
        return userRepository.count();
    }

    @Override
    public void seedUsers(List<UserSeedDto> users) {
        users
                .stream()
                .filter(validationUtil::isValid)
                .map(userSeedDto -> modelMapper.map(userSeedDto, User.class))
                .forEach(userRepository::save);
    }

    @Override
    public User getRandomUser() {
        long randomId = ThreadLocalRandom.current().nextLong(1, userRepository.count() + 1);
        return userRepository.findById(randomId).orElse(null);
    }

    @Override
    public UsersViewRootDto findUsersWithMoreThenOneSoldProduct() {
        UsersViewRootDto viewRootDto = new UsersViewRootDto();
        viewRootDto.setProducts(userRepository.findAllUsersWithMoreThanOneSoldProduct()
                .stream().map(user -> modelMapper.map(user, UserWithProductsDto.class))
                .collect(Collectors.toList()));

        return viewRootDto;
    }
}
