package com.example.exercise_6_springdataautomappingobjects.service;

import com.example.exercise_6_springdataautomappingobjects.model.entity.Game;
import com.example.exercise_6_springdataautomappingobjects.model.entity.User;

import java.util.Set;

public interface OrderService {
    void registerOrder(User loggedUser, Set<Game> games);
}
