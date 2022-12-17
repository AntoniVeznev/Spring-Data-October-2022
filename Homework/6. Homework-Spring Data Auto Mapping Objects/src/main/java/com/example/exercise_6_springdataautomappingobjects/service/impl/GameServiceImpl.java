package com.example.exercise_6_springdataautomappingobjects.service.impl;

import com.example.exercise_6_springdataautomappingobjects.model.dto.GameAddDto;
import com.example.exercise_6_springdataautomappingobjects.model.entity.Game;
import com.example.exercise_6_springdataautomappingobjects.model.entity.User;
import com.example.exercise_6_springdataautomappingobjects.repository.GameRepository;
import com.example.exercise_6_springdataautomappingobjects.repository.OrderRepository;
import com.example.exercise_6_springdataautomappingobjects.repository.UserRepository;
import com.example.exercise_6_springdataautomappingobjects.service.GameService;
import com.example.exercise_6_springdataautomappingobjects.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final UserServiceImpl userServiceImpl;
    private final OrderServiceImpl orderServiceImpl;
    private final OrderRepository orderRepository;
    Scanner scanner = new Scanner(System.in);


    public GameServiceImpl(GameRepository gameRepository, UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil, UserServiceImpl userServiceImpl, OrderServiceImpl orderServiceImpl, OrderRepository orderRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.userServiceImpl = userServiceImpl;
        this.orderServiceImpl = orderServiceImpl;
        this.orderRepository = orderRepository;
    }

    @Override
    public void addGame(GameAddDto gameAddDto) {
        Set<ConstraintViolation<GameAddDto>> violations = validationUtil.getViolations(gameAddDto);
        if (!violations.isEmpty()) {
            violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            return;
        }
        Game game = modelMapper.map(gameAddDto, Game.class);
        game.setReleaseDate(LocalDate.parse(gameAddDto.getReleaseDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        gameRepository.save(game);
        System.out.printf("Added %s%n", game.getTitle());

    }

    @Override
    public void editGame(String gameId, String price, String size) {
        Long id = Long.parseLong(gameId);
        String[] stringPrice = price.split("=");
        String[] stringSize = size.split("=");
        BigDecimal newPrice = new BigDecimal(stringPrice[1]);
        Double newSize = Double.parseDouble(stringSize[1]);
        Game game = gameRepository.findById(id).orElse(null);
        if (game == null) {
            System.out.println("We dont have such game in DataBase!!!!!!!!!!!!!!!");
            return;
        }
        game.setPrice(newPrice);
        game.setSize(newSize);
        gameRepository.save(game);
        System.out.printf("Edited %s%n", game.getTitle());
    }

    @Override
    public void deleteGame(String gameId) {
        Long id = Long.parseLong(gameId);
        Game game = gameRepository.findById(id).orElse(null);
        if (game == null) {
            System.out.println("We dont have such game in DataBase!!!!!!!!!!!!!!!");
            return;
        }
        gameRepository.delete(game);
        System.out.printf("Deleted %s%n", game.getTitle());
    }

    @Override
    public void getAllGames() {
        List<Game> listOfAllGames = gameRepository.findAll();
        listOfAllGames
                .forEach(game ->
                        System.out.printf("%s %.2f%n",
                                game.getTitle(),
                                game.getPrice()));
    }

    @Override
    public void getSingleGameByTitle(String command) {
        Game game = gameRepository.findGameByTitle(command);
        LocalDate releaseDate = game.getReleaseDate();
        String releaseDateConverted = releaseDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        System.out.printf("Title: %s%n" +
                        "Price: %.2f%n" +
                        "Description: %s%n" +
                        "Release date: %s%n",
                game.getTitle(),
                game.getPrice(),
                game.getDescription(),
                releaseDateConverted);
    }

    @Override
    public void getAllGamesByUser() {
        User loggedInUser = userServiceImpl.loggedInUser;
        if (loggedInUser == null) {
            System.out.println("Please log in !!!");
            return;
        }

        if (loggedInUser.getGames().size() == 0) {
            System.out.printf("%s dont have any games yet!%n", loggedInUser.getFullName());
            System.out.println("Do u wont buy more games from game store? YES or NO");
            String answer = scanner.nextLine().toLowerCase();
            if (answer.equals("yes")){
                letsBuyGames();
            }else {
                loggedInUser.getGames()
                        .stream()
                        .map(game -> String.format("%s%n", game.getTitle()))
                        .forEach(System.out::println);
            }
        } else {
            System.out.println("Do u wont buy more games from game store? YES or NO");
            String answer = scanner.nextLine().toLowerCase();
            if (answer.equals("yes")){
                letsBuyGames();
            } else {
                loggedInUser.getGames()
                        .stream()
                        .map(game -> String.format("%s%n", game.getTitle()))
                        .forEach(System.out::println);
            }
        }


    }

    private void letsBuyGames() {
        System.out.println("Please type name of game you wona buy");
        String gameName = scanner.nextLine();
        User loggedInUser = userServiceImpl.loggedInUser;
        loggedInUser.getGames().add(gameRepository.findGameByTitle(gameName));
        System.out.println("Game bought!");
        loggedInUser.getGames().stream().map(game -> String.format("%s", game.getTitle()))
                .forEach(System.out::println);

        orderServiceImpl.registerOrder(loggedInUser, loggedInUser.getGames());

    }


}