package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.MechanicsSeedRoodDtoJSON;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.repository.MechanicRepository;
import softuni.exam.service.MechanicService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class MechanicServiceImpl implements MechanicService {
    private final static String MECHANIC_PATH = "src/main/resources/files/json/mechanics.json";
    private MechanicRepository mechanicRepository;
    private ValidationUtil validationUtil;
    private ModelMapper modelMapper;
    private Gson gson;

    public MechanicServiceImpl(MechanicRepository mechanicRepository, ValidationUtil validationUtil, ModelMapper modelMapper, Gson gson) {
        this.mechanicRepository = mechanicRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return mechanicRepository.count() > 0;
    }

    @Override
    public String readMechanicsFromFile() throws IOException {
        return Files.readString(Path.of(MECHANIC_PATH));
    }

    @Override
    public String importMechanics() throws IOException {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(gson
                        .fromJson(readMechanicsFromFile(),
                                MechanicsSeedRoodDtoJSON[].class))
                .filter(mechanicsSeedRoodDtoJSON -> {
                    boolean isValid = validationUtil.isValid(mechanicsSeedRoodDtoJSON)
                            && !ifSameEmailExist(mechanicsSeedRoodDtoJSON.getEmail());
                    if (isValid) {
                        builder.append(String.format("Successfully imported mechanic %s %s%n",
                                mechanicsSeedRoodDtoJSON.getFirstName(),
                                mechanicsSeedRoodDtoJSON.getLastName()));
                    } else {
                        builder.append("Invalid mechanic").append(System.lineSeparator());
                    }

                    return isValid;
                })
                .map(mechanicsSeedRoodDtoJSON -> modelMapper.map(mechanicsSeedRoodDtoJSON, Mechanic.class))
                .forEach(mechanicRepository::save);


        return builder.toString();
    }

    private boolean ifSameEmailExist(String email) {
        return mechanicRepository.existsMechanicByEmail(email);
    }
}
