package com.example.football.service.impl;

import com.example.football.models.dto.PlayerStatIdDtoXML;
import com.example.football.models.dto.PlayerTeamNameDtoXML;
import com.example.football.models.dto.PlayerTownNameDtoXML;
import com.example.football.models.dto.PlayersRootSeedDtoXML;
import com.example.football.models.entity.Player;
import com.example.football.models.entity.Stat;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.PlayerRepository;
import com.example.football.repository.StatRepository;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.PlayerService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

@Service
public class PlayerServiceImpl implements PlayerService {
    private static final String PLAYERS_PATH_FILE = "src/main/resources/files/xml/players.xml";
    private PlayerRepository playerRepository;
    private TownRepository townRepository;
    private TeamRepository teamRepository;
    private StatRepository statRepository;
    private ModelMapper modelMapper;
    private ValidationUtil validationUtil;
    private XmlParser xmlParser;

    public PlayerServiceImpl(PlayerRepository playerRepository, TownRepository townRepository, TeamRepository teamRepository, StatRepository statRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.playerRepository = playerRepository;
        this.townRepository = townRepository;
        this.teamRepository = teamRepository;
        this.statRepository = statRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files.readString(Path.of(PLAYERS_PATH_FILE));
    }

    @Override
    public String importPlayers() throws JAXBException, FileNotFoundException {
        StringBuilder builder = new StringBuilder();
        PlayersRootSeedDtoXML playersRootSeedDtoXML = xmlParser.fromFile(PLAYERS_PATH_FILE, PlayersRootSeedDtoXML.class);

        playersRootSeedDtoXML
                .getPlayer()
                .stream()
                .filter(playerSeedDtoXML -> {
                    boolean isValid = validationUtil.isValid(playerSeedDtoXML)
                            && !ifPlayerEmailExist(playerSeedDtoXML.getEmail());
                    if (isValid) {
                        builder
                                .append(String.format("Successfully imported Player %s %s - %s%n",
                                        playerSeedDtoXML.getFirstName(),
                                        playerSeedDtoXML.getLastName(),
                                        playerSeedDtoXML.getPosition()));
                    } else {
                        builder.append("Invalid Player").append(System.lineSeparator());
                    }
                    return isValid;
                })
                .map(playerSeedDtoXML -> {
                    Player player = modelMapper.map(playerSeedDtoXML, Player.class);
                    player.setTown(findTown(playerSeedDtoXML.getTown()));
                    player.setTeam(findTeam(playerSeedDtoXML.getTeam()));
                    player.setStat(findStat(playerSeedDtoXML.getStat()));
                    return player;
                })
                .forEach(playerRepository::save);
        return builder.toString();
    }

    private Stat findStat(PlayerStatIdDtoXML stat) {
        return statRepository.findStatById(stat.getId());
    }

    private Team findTeam(PlayerTeamNameDtoXML team) {
        return teamRepository.findTeamByName(team.getName());
    }

    private Town findTown(PlayerTownNameDtoXML town) {
        return townRepository.findTownByName(town.getName());
    }

    private boolean ifPlayerEmailExist(String email) {
        return playerRepository.existsPlayerByEmail(email);
    }

    @Override
    public String exportBestPlayers() {
        StringBuilder builder = new StringBuilder();
        LocalDate lowerDate = LocalDate.of(1995, 1, 1);
        LocalDate upperDate = LocalDate.of(2003, 1, 1);
        playerRepository
                .allPlayersWithSomeCriteria()
                .forEach(player -> {
                    if (player.getBirthDate().isAfter(lowerDate)
                            && player.getBirthDate().isBefore(upperDate)) {
                        builder
                                .append(String.format("Player - %s %s\n" +
                                                "\tPosition - %s\n" +
                                                "\tTeam - %s\n" +
                                                "\tStadium - %s%n",
                                        player.getFirstName(),
                                        player.getLastName(),
                                        player.getPosition().name(),
                                        player.getTeam().getName(),
                                        player.getTeam().getStadiumName()));
                    }
                });
        return builder.toString();
    }
}