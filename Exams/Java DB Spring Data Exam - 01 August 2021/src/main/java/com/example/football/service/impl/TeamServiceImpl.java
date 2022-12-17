package com.example.football.service.impl;

import com.example.football.models.dto.TeamSeedDtoJSON;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.TeamService;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class TeamServiceImpl implements TeamService {
    private static final String TEAMS_PATH_FILE = "src/main/resources/files/json/teams.json";
    private TeamRepository teamRepository;
    private TownRepository townRepository;
    private ModelMapper modelMapper;
    private ValidationUtil validationUtil;
    private Gson gson;

    public TeamServiceImpl(TeamRepository teamRepository, TownRepository townRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.teamRepository = teamRepository;
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return teamRepository.count() > 0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        return Files.readString(Path.of(TEAMS_PATH_FILE));
    }

    @Override
    public String importTeams() throws IOException {
        StringBuilder builder = new StringBuilder();
        TeamSeedDtoJSON[] teamSeedDtoJSONS = gson.fromJson(readTeamsFileContent(), TeamSeedDtoJSON[].class);
        Arrays.stream(teamSeedDtoJSONS)
                .filter(teamSeedDtoJSON -> {
                    boolean isValid = validationUtil.isValid(teamSeedDtoJSON)
                            && !ifTeamNameExist(teamSeedDtoJSON.getName());
                    if (isValid) {
                        builder
                                .append(String.format("Successfully imported Team %s - %d%n",
                                        teamSeedDtoJSON.getName(),
                                        teamSeedDtoJSON.getFanBase()));
                    } else {
                        builder.append("Invalid Team").append(System.lineSeparator());
                    }

                    return isValid;
                })
                .map(teamSeedDtoJSON -> {
                    Team team = modelMapper.map(teamSeedDtoJSON,Team.class);
                        team.setTown(findTownIdByName(teamSeedDtoJSON.getTownName()));
                        return team;
                })
                .forEach(teamRepository::save);

        return builder.toString();
    }

    private Town findTownIdByName(String townName) {
        return townRepository.findTownByName(townName);
    }

    private boolean ifTeamNameExist(String name) {
        return teamRepository.existsTeamByName(name);
    }
}
