package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CityRootDtoJSON;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CityService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class CityServiceImpl implements CityService {
    private static final String CITIES_PATH_FILE = "src/main/resources/files/json/cities.json";
    private CityRepository cityRepository;
    private CountryRepository countryRepository;
    private ValidationUtil validationUtil;
    private ModelMapper modelMapper;
    private Gson gson;

    public CityServiceImpl(CityRepository cityRepository, CountryRepository countryRepository, ValidationUtil validationUtil, ModelMapper modelMapper, Gson gson) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return cityRepository.count() > 0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        return Files.readString(Path.of(CITIES_PATH_FILE));
    }

    @Override
    public String importCities() throws IOException {
        StringBuilder builder = new StringBuilder();
        CityRootDtoJSON[] cityRootDtoJSONS =
                gson.fromJson(readCitiesFileContent(), CityRootDtoJSON[].class);
        Arrays.stream(cityRootDtoJSONS)
                .filter(cityRootDtoJSON -> {
                    boolean isValid = validationUtil.isValid(cityRootDtoJSON)
                            && !ifCityExists(cityRootDtoJSON.getCityName());

                    if (isValid) {
                        builder
                                .append(String.format("Successfully imported city %s - %d%n",
                                        cityRootDtoJSON.getCityName(),
                                        cityRootDtoJSON.getPopulation()));
                    } else {
                        builder
                                .append("Invalid city").append(System.lineSeparator());
                    }
                    return isValid;
                }).map(cityRootDtoJSON -> {
                    City city = modelMapper.map(cityRootDtoJSON, City.class);
                    city.setCountry(findCountryById(cityRootDtoJSON.getCountry()));
                    return city;
                })
                .forEach(cityRepository::save);


        return builder.toString();
    }

    private Country findCountryById(Long country) {
        return countryRepository.findById(country).orElse(null);
    }

    private boolean ifCityExists(String cityName) {
        return cityRepository.existsByCityName(cityName);
    }
}
