package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PlanesSeedRootDtoXML;
import softuni.exam.models.entity.Plane;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.service.PlaneService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PlaneServiceImpl implements PlaneService {
    private final static String PLANES_PATH_FILE = "src/main/resources/files/xml/planes.xml";
    private PlaneRepository planeRepository;
    private ModelMapper modelMapper;
    private ValidationUtil validationUtil;
    private XmlParser xmlParser;

    public PlaneServiceImpl(PlaneRepository planeRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.planeRepository = planeRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return planeRepository.count() > 0;
    }

    @Override
    public String readPlanesFileContent() throws IOException {
        return Files.readString(Path.of(PLANES_PATH_FILE));
    }

    @Override
    public String importPlanes() throws JAXBException, FileNotFoundException {
        StringBuilder builder = new StringBuilder();
        xmlParser.fromFile(PLANES_PATH_FILE, PlanesSeedRootDtoXML.class)
                .getPlanes()
                .stream()
                .filter(planeDtoXml -> {
                    boolean isValid = validationUtil.isValid(planeDtoXml);
                    if (isValid) {
                        builder
                                .append(String.format("Successfully imported Plane %s%n",
                                        planeDtoXml.getRegisterNumber()));
                    } else {
                        builder.append("Invalid Plane").append(System.lineSeparator());
                    }
                    return isValid;
                })
                .map(planeDtoXml -> modelMapper.map(planeDtoXml, Plane.class))
                .forEach(planeRepository::save);


        return builder.toString();
    }
}
