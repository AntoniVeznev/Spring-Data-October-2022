package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PartsSeedRootDtoJSON;
import softuni.exam.models.entity.Part;
import softuni.exam.repository.PartRepository;
import softuni.exam.service.PartService;
import softuni.exam.util.ValidationUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class PartServiceImpl implements PartService {
    private final static String PARTS_PATH = "src/main/resources/files/json/parts.json";
    private ValidationUtil validationUtil;
    private PartRepository partRepository;
    private ModelMapper modelMapper;
    private Gson gson;

    public PartServiceImpl(ValidationUtil validationUtil, PartRepository partRepository, ModelMapper modelMapper, Gson gson) {
        this.validationUtil = validationUtil;
        this.partRepository = partRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return partRepository.count() > 0;
    }

    @Override
    public String readPartsFileContent() throws IOException {
        return Files.readString(Path.of(PARTS_PATH));
    }

    @Override
    public String importParts() throws IOException {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(gson
                        .fromJson(readPartsFileContent(),
                                PartsSeedRootDtoJSON[].class))
                .filter(partsSeedRootDtoJSON -> {
                    boolean isValid = validationUtil.isValid(partsSeedRootDtoJSON)
                            && !ifPartWithThatNameExist(partsSeedRootDtoJSON.getPartName());
                    if (isValid) {
                        builder.append(String.format("Successfully imported part %s - %.2f%n",
                                partsSeedRootDtoJSON.getPartName(),
                                partsSeedRootDtoJSON.getPrice()));
                    } else {
                        builder.append("Invalid part").append(System.lineSeparator());
                    }
                    return isValid;
                })
                .map(partsSeedRootDtoJSON -> modelMapper.map(partsSeedRootDtoJSON, Part.class))
                .forEach(partRepository::save);


        return builder.toString();
    }

    private boolean ifPartWithThatNameExist(String partName) {
        return partRepository.existsPartByPartName(partName);
    }
}
