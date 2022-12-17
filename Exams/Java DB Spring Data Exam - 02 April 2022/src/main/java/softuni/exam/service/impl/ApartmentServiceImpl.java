package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ApartmentRootSeedDtoXML;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.ApartmentService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ApartmentServiceImpl implements ApartmentService {

    private final static String APARTMENTS_FILE_PATH = "src/main/resources/files/xml/apartments.xml";
    private ApartmentRepository apartmentRepository;
    private TownRepository townRepository;
    private ModelMapper modelMapper;
    private ValidationUtil validationUtil;
    private Gson gson;
    private XmlParser xmlParser;

    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, TownRepository townRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson, XmlParser xmlParser) {
        this.apartmentRepository = apartmentRepository;
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return apartmentRepository.count() > 0;
    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return Files.readString(Path.of(APARTMENTS_FILE_PATH));
    }

    @Override
    public String importApartments() throws IOException, JAXBException {
        StringBuilder builder = new StringBuilder();
        ApartmentRootSeedDtoXML apartmentRootSeedDtoXML = xmlParser.fromFile(APARTMENTS_FILE_PATH, ApartmentRootSeedDtoXML.class);

        apartmentRootSeedDtoXML
                .getApartments()
                .stream()
                .filter(apartmentDto -> {
                    boolean isValid = validationUtil.isValid(apartmentDto)
                            && !testThis(apartmentDto.getTown(), apartmentDto.getArea());

                    if (isValid) {
                        builder.append(String.format("Successfully imported apartment %s - %.2f%n",
                                apartmentDto.getApartmentType(),
                                apartmentDto.getArea()));
                    } else {
                        builder.append("Invalid apartment").append(System.lineSeparator());
                    }
                    return isValid;
                })
                .map(apartmentDto -> {
                    Apartment apartment = modelMapper.map(apartmentDto, Apartment.class);
                    apartment.setTown(findTown(apartmentDto.getTown()));
                    return apartment;
                })
                .forEach(apartmentRepository::save);

        return builder.toString();
    }

    private boolean testThis(String town, Double area) {
        return apartmentRepository.existsByTownAndArea(townRepository.findTownByTownName(town), area);
    }

    private Town findTown(String town) {
        return townRepository.findTownByTownName(town);
    }


}
