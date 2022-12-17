package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ForecastRootDtoXML;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.DayOfWeekEnums;
import softuni.exam.models.entity.Forecast;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.ForecastRepository;
import softuni.exam.service.ForecastService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ForecastServiceImpl implements ForecastService {
    private final static String FORECAST_PATH_FILE = "src/main/resources/files/xml/forecasts.xml";
    private ForecastRepository forecastRepository;
    private CityRepository cityRepository;
    private ModelMapper modelMapper;
    private XmlParser xmlParser;
    private ValidationUtil validationUtil;

    public ForecastServiceImpl(ForecastRepository forecastRepository, CityRepository cityRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil) {
        this.forecastRepository = forecastRepository;
        this.cityRepository = cityRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return forecastRepository.count() > 0;
    }

    @Override
    public String readForecastsFromFile() throws IOException {
        return Files.readString(Path.of(FORECAST_PATH_FILE));
    }

    @Override
    public String importForecasts() throws IOException, JAXBException {
        StringBuilder builder = new StringBuilder();

        ForecastRootDtoXML forecastRootDtoXML = xmlParser.fromFile(FORECAST_PATH_FILE, ForecastRootDtoXML.class);
        forecastRootDtoXML
                .getForecasts()
                .stream()
                .filter(forecastDtoXML -> {
                    boolean isValid = validationUtil.isValid(forecastDtoXML)
                            && !findTest(forecastDtoXML.getCity(), forecastDtoXML.getDayOfWeek());
                    if (isValid) {
                        builder.append(String.format("Successfully import forecast %s - %.2f%n",
                                forecastDtoXML.getDayOfWeek(),
                                forecastDtoXML.getMaxTemperature()));
                    } else {
                        builder.append("Invalid forecast").append(System.lineSeparator());
                    }
                    return isValid;
                })
                .map(forecastDtoXML -> {
                    Forecast forecast = modelMapper.map(forecastDtoXML, Forecast.class);
                    forecast.setCity(findItPleaseAndEndThis(forecastDtoXML.getCity()));
                    return forecast;
                })
                .forEach(forecastRepository::save);


        return builder.toString();
    }

    private City findItPleaseAndEndThis(Long city) {
        return cityRepository.findById(city).orElse(null);
    }

    private boolean findTest(Long city, DayOfWeekEnums dayOfWeek) {
        return forecastRepository.existsByIdAndDayOfWeek(city, dayOfWeek);
    }


    @Override
    public String exportForecasts() {
        StringBuilder builder = new StringBuilder();
        List<Forecast> test = forecastRepository.test();
        for (Forecast forecast : test) {
            builder.append(String.format("City: %s:%n",forecast.getCity().getCityName()));
            builder.append(String.format
                   ("-min temperature: %.2f\n" +
                    "--max temperature: %.2f\n" +
                    "---sunrise: %s\n" +
                    "----sunset: %s%n",
                   forecast.getMinTemperature(),
                    forecast.getMaxTemperature(),
                    forecast.getSunrise(),
                    forecast.getSunset()));
        }

        return builder.toString();
    }
}
