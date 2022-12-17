package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CountryRootDtoJSON;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class CountryServiceImpl implements CountryService {

    private static final String COUNTRIES_PATH_FILE = "src/main/resources/files/json/countries.json";
    private CountryRepository countryRepository;
    private ModelMapper modelMapper;
    private ValidationUtil validationUtil;
    private Gson gson;

    public CountryServiceImpl(CountryRepository countryRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return countryRepository.count() > 0;
    }

    @Override
    public String readCountriesFromFile() throws IOException {
        return Files.readString(Path.of(COUNTRIES_PATH_FILE));
    }

    @Override
    public String importCountries() throws IOException {
        StringBuilder builder = new StringBuilder();
        CountryRootDtoJSON[] countryRootDtoJSONS =
                gson.fromJson(readCountriesFromFile(), CountryRootDtoJSON[].class);
        Arrays.stream(countryRootDtoJSONS)
                .filter(countryRootDtoJSON -> {
                    boolean isValid = validationUtil.isValid(countryRootDtoJSON)
                            && !isCountryInDatabase(countryRootDtoJSON.getCountryName());
                    if (isValid) {
                        builder.
                                append(String.format("Successfully imported country %s - %s%n",
                                        countryRootDtoJSON.getCountryName(),
                                        countryRootDtoJSON.getCurrency()));
                    } else {
                        builder.append("Invalid country").append(System.lineSeparator());
                    }

                    return isValid;
                })
                .map(countryRootDtoJSON -> modelMapper.map(countryRootDtoJSON, Country.class))
                .forEach(countryRepository::save);


        return builder.toString();
    }

    private boolean isCountryInDatabase(String countryName) {
        return countryRepository.existsByCountryName(countryName);
    }
}
