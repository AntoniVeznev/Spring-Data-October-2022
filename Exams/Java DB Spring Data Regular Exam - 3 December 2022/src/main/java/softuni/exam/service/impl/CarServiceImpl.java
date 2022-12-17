package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CarsSeedRootDtoXML;
import softuni.exam.models.entity.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class CarServiceImpl implements CarService {
    private static final String CARS_PATH = "src/main/resources/files/xml/cars.xml";
    private CarRepository carRepository;
    private ValidationUtil validationUtil;
    private ModelMapper modelMapper;
    private XmlParser xmlParser;

    public CarServiceImpl(CarRepository carRepository, ValidationUtil validationUtil, ModelMapper modelMapper, XmlParser xmlParser) {
        this.carRepository = carRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }


    @Override
    public boolean areImported() {
        return carRepository.count() > 0;
    }

    @Override
    public String readCarsFromFile() throws IOException {
        return Files.readString(Path.of(CARS_PATH));
    }

    @Override
    public String importCars() throws IOException, JAXBException {
        StringBuilder builder = new StringBuilder();
        xmlParser
                .fromFile(CARS_PATH,
                        CarsSeedRootDtoXML.class)
                .getCars()
                .stream()
                .filter(carDtoXML -> {
                    boolean isValid = validationUtil.isValid(carDtoXML)
                            && !ifCarWithPlateNumberExist(carDtoXML.getPlateNumber());
                    if (isValid) {
                        builder.append(String.format("Successfully imported car %s - %s%n",
                                carDtoXML.getCarMake(),
                                carDtoXML.getCarModel()));
                    } else {
                        builder.append("Invalid car").append(System.lineSeparator());
                    }
                    return isValid;
                })
                .map(carDtoXML -> modelMapper.map(carDtoXML, Car.class))
                .forEach(carRepository::save);


        return builder.toString();
    }

    private boolean ifCarWithPlateNumberExist(String plateNumber) {
        return carRepository.existsCarByPlateNumber(plateNumber);
    }
}
