package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TownSeedDtoJSON;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class TownServiceImpl implements TownService {
    private final static String TOWNS_PATH_FILE = "src/main/resources/files/json/towns.json";
    private TownRepository townRepository;
    private ModelMapper modelMapper;
    private ValidationUtil validationUtil;
    private Gson gson;

    public TownServiceImpl(TownRepository townRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
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
        Arrays.stream(gson.fromJson(readTownsFileContent(), TownSeedDtoJSON[].class))
                .filter(townSeedDtoJSON -> {
                    boolean isValid = validationUtil.isValid(townSeedDtoJSON);
                    if (isValid) {
                        builder
                                .append(String.format("Successfully imported Town %s - %d%n",
                                        townSeedDtoJSON.getName(),
                                        townSeedDtoJSON.getPopulation()));
                    } else {
                        builder
                                .append("Invalid Town")
                                .append(System.lineSeparator());
                    }
                    return isValid;
                })
                .map(townSeedDtoJSON -> modelMapper.map(townSeedDtoJSON, Town.class))
                .forEach(townRepository::save);

        return builder.toString();
    }
}