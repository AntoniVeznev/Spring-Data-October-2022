package com.example.exercise_6_springdataautomappingobjects.service;

import com.example.exercise_6_springdataautomappingobjects.model.dto.GameAddDto;

public interface GameService {
    void addGame(GameAddDto gameAddDto);

    void editGame(String gameId, String price, String size);

    void deleteGame(String gameId);

    void getAllGames();

    void getSingleGameByTitle(String command);

    void getAllGamesByUser();

}