package com.example.exercise_6_springdataautomappingobjects.repository;

import com.example.exercise_6_springdataautomappingobjects.model.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Game findGameByTitle(String title);



}