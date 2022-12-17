package com.example.exercise_6_springdataautomappingobjects.repository;

import com.example.exercise_6_springdataautomappingobjects.model.entity.Game;
import com.example.exercise_6_springdataautomappingobjects.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


}
