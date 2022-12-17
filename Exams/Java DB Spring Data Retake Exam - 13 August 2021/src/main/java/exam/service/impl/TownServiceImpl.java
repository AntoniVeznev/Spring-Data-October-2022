package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dto.TownSeedRootDtoXML;
import exam.model.entity.Town;
import exam.repository.TownRepository;
import exam.service.TownService;
import exam.util.ValidationUtil;
import exam.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TownServiceImpl implements TownService {
    private static final String TOWNS_PATH_FILE = "src/main/resources/files/xml/towns.xml";

    private TownRepository townRepository;
    private ValidationUtil validationUtil;
    private ModelMapper modelMapper;
    private Gson gson;
    private XmlParser xmlParser;

    public TownServiceImpl(TownRepository townRepository, ValidationUtil validationUtil, ModelMapper modelMapper, Gson gson, XmlParser xmlParser) {
        this.townRepository = townRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.xmlParser = xmlParser;
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
    public String importTowns() throws JAXBException, FileNotFoundException {
        StringBuilder builder = new StringBuilder();
        xmlParser
                .fromFile(TOWNS_PATH_FILE, TownSeedRootDtoXML.class)
                .getTown()
                .stream()
                .filter(townDtoXML -> {
                    boolean isValid = validationUtil.isValid(townDtoXML);
                    if (isValid) {
                        builder
                                .append(String.format("Successfully imported Town %s%n",
                                        townDtoXML.getName()));
                    } else {
                        builder
                                .append("Invalid town").append(System.lineSeparator());
                    }

                    return isValid;
                })
                .map(townDtoXML -> modelMapper.map(townDtoXML, Town.class))
                .forEach(townRepository::save);


        return builder.toString();
    }
}
