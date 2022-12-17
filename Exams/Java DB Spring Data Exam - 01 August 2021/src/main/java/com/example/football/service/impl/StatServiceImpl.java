package com.example.football.service.impl;

import com.example.football.models.dto.StatsRootSeedDtoXML;
import com.example.football.models.entity.Stat;
import com.example.football.repository.StatRepository;
import com.example.football.service.StatService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class StatServiceImpl implements StatService {
    private static final String STATS_PATH_FILE = "src/main/resources/files/xml/stats.xml";
    private StatRepository statRepository;
    private ValidationUtil validationUtil;
    private ModelMapper modelMapper;
    private XmlParser xmlParser;


    public StatServiceImpl(StatRepository statRepository, ValidationUtil validationUtil, ModelMapper modelMapper, XmlParser xmlParser) {
        this.statRepository = statRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return statRepository.count() > 0;
    }

    @Override
    public String readStatsFileContent() throws IOException {
        return Files.readString(Path.of(STATS_PATH_FILE));
    }

    @Override
    public String importStats() throws JAXBException, FileNotFoundException {
        StringBuilder builder = new StringBuilder();

        StatsRootSeedDtoXML statsRootSeedDtoXML = xmlParser.fromFile(STATS_PATH_FILE, StatsRootSeedDtoXML.class);

        statsRootSeedDtoXML
                .getStat()
                .stream()
                .filter(statsDtoXML -> {
                    boolean isValid = validationUtil.isValid(statsDtoXML)
                            && !ifSameStatExistsInDb(statsDtoXML.getPassing(), statsDtoXML.getShooting(), statsDtoXML.getEndurance());
                    if (isValid) {
                        builder
                                .append(String.format("Successfully imported Stat %.2f - %.2f - %.2f%n",
                                        statsDtoXML.getShooting(),
                                        statsDtoXML.getPassing(),
                                        statsDtoXML.getEndurance()));

                    } else {
                        builder.append("Invalid Stat").append(System.lineSeparator());
                    }
                    return isValid;
                })
                .map(statsDtoXML -> modelMapper.map(statsDtoXML, Stat.class))
                .forEach(statRepository::save);

        return builder.toString();
    }

    private boolean ifSameStatExistsInDb(Float passing, Float shooting, Float endurance) {
        return statRepository.existsStatByPassingAndShootingAndEndurance(passing, shooting, endurance);
    }
}
