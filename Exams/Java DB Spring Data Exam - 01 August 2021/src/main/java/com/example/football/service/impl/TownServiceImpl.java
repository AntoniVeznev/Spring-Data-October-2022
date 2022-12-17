package com.example.football.service.impl;

import com.example.football.models.dto.TownSeedDtoJSON;
import com.example.football.models.entity.Town;
import com.example.football.repository.TownRepository;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;


@Service
public class TownServiceImpl implements TownService {
    private static final String TOWNS_PATH_FILE = "src/main/resources/files/json/towns.json";
    private TownRepository townRepository;
    private ValidationUtil validationUtil;
    private ModelMapper modelMapper;
    private Gson gson;

    public TownServiceImpl(TownRepository townRepository, ValidationUtil validationUtil, ModelMapper modelMapper, Gson gson) {
        this.townRepository = townRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }


    @Override
    public boolean areImported() {
        return townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(Path.of(TOWNS_PATH_FILE));
    }

    @Override
    public String importTowns() throws IOException {
        StringBuilder builder = new StringBuilder();
        TownSeedDtoJSON[] townSeedDtoJSONS = gson.fromJson(readTownsFileContent(), TownSeedDtoJSON[].class);
        Arrays.stream(townSeedDtoJSONS)
                .filter(townSeedDtoJSON -> {
                    boolean isValid = validationUtil.isValid(townSeedDtoJSON)
                            && !ifTownExist(townSeedDtoJSON.getName());
                    if (isValid) {
                        builder
                                .append(String.format("Successfully imported Town %s - %d%n",
                                        townSeedDtoJSON.getName(),
                                        townSeedDtoJSON.getPopulation()));
                    } else {
                        builder.append("Invalid Town").append(System.lineSeparator());
                    }

                    return isValid;
                })
                .map(townSeedDtoJSON -> modelMapper.map(townSeedDtoJSON, Town.class))
                .forEach(townRepository::save);


        return builder.toString();
    }

    private boolean ifTownExist(String name) {
        return townRepository.existsTownByName(name);
    }
}
