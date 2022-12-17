package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.AgentsRootDtoJSON;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.AgentService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class AgentServiceImpl implements AgentService {

    private static final String AGENTS_PATH_NAME = "src/main/resources/files/json/agents.json";
    private AgentRepository agentRepository;
    private TownRepository townRepository;
    private ModelMapper modelMapper;
    private ValidationUtil validationUtil;
    private Gson gson;

    public AgentServiceImpl(AgentRepository agentRepository, TownRepository townRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.agentRepository = agentRepository;
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return agentRepository.count() > 0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        return Files.readString(Path.of(AGENTS_PATH_NAME));
    }

    @Override
    public String importAgents() throws IOException {
        StringBuilder builder = new StringBuilder();
        AgentsRootDtoJSON[] agentsRootDtoJSONS = gson.fromJson(readAgentsFromFile(), AgentsRootDtoJSON[].class);
        Arrays.stream(agentsRootDtoJSONS)
                .filter(agentsRootDtoJSON -> {
                    boolean isValid = validationUtil.isValid(agentsRootDtoJSON)
                            && !ifAgentFirstNameExistsInDataBase(agentsRootDtoJSON.getFirstName());
                    if (isValid) {
                        builder.append(String.format("Successfully imported agent - %s %s%n",
                                agentsRootDtoJSON.getFirstName(),
                                agentsRootDtoJSON.getLastName()));
                    } else {
                        builder.append("Invalid agent").append(System.lineSeparator());
                    }
                    return isValid;
                })
                .map(agentsRootDtoJSON -> {
                    Agent agent = modelMapper.map(agentsRootDtoJSON, Agent.class);
                    agent.setTown(findTownByStringName(agentsRootDtoJSON.getTown()));
                    return agent;
                })
                .forEach(agentRepository::save);
        return builder.toString();
    }

    private Town findTownByStringName(String town) {
        return townRepository.findTownByTownName(town);
    }


    private boolean ifAgentFirstNameExistsInDataBase(String firstName) {
        return agentRepository.existsByFirstName(firstName);
    }


}
