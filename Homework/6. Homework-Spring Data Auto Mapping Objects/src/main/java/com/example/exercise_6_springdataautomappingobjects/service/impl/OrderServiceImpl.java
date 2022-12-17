package com.example.exercise_6_springdataautomappingobjects.service.impl;

import com.example.exercise_6_springdataautomappingobjects.model.entity.Game;
import com.example.exercise_6_springdataautomappingobjects.model.entity.Order;
import com.example.exercise_6_springdataautomappingobjects.model.entity.User;
import com.example.exercise_6_springdataautomappingobjects.repository.OrderRepository;
import com.example.exercise_6_springdataautomappingobjects.repository.UserRepository;
import com.example.exercise_6_springdataautomappingobjects.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private Order order;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;


    }

    @Override
    public void registerOrder(User loggedUser, Set<Game> games) {
        this.order = new Order();
        order.setBuyer(loggedUser);
        order.setGames(games);
        orderRepository.save(order);
    }
}
