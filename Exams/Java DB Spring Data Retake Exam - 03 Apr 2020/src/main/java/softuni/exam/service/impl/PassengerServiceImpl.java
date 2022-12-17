package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PassengerSeedDtoJSON;
import softuni.exam.models.entity.Passenger;
import softuni.exam.models.entity.Ticket;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.TicketRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class PassengerServiceImpl implements PassengerService {

    private final static String PASSENGERS_PATH_FILE = "src/main/resources/files/json/passengers.json";
    private PassengerRepository passengerRepository;
    private TicketRepository ticketRepository;
    private TownRepository townRepository;
    private ModelMapper modelMapper;
    private ValidationUtil validationUtil;
    private Gson gson;

    public PassengerServiceImpl(PassengerRepository passengerRepository, TicketRepository ticketRepository, TownRepository townRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.passengerRepository = passengerRepository;
        this.ticketRepository = ticketRepository;
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return passengerRepository.count() > 0;
    }

    @Override
    public String readPassengersFileContent() throws IOException {
        return Files.readString(Path.of(PASSENGERS_PATH_FILE));
    }

    @Override
    public String importPassengers() throws IOException {
        StringBuilder builder = new StringBuilder();

        Arrays.stream(gson.fromJson(readPassengersFileContent(), PassengerSeedDtoJSON[].class))
                .filter(passengerSeedDtoJSON -> {
                    boolean isValid = validationUtil.isValid(passengerSeedDtoJSON);
                    if (isValid) {
                        builder
                                .append(String.format("Successfully imported Passenger %s - %s%n",
                                        passengerSeedDtoJSON.getLastName(),
                                        passengerSeedDtoJSON.getEmail()));
                    } else {
                        builder
                                .append("Invalid Passenger").append(System.lineSeparator());
                    }
                    return isValid;
                })
                .map(passengerSeedDtoJSON -> {
                    Passenger passenger = modelMapper.map(passengerSeedDtoJSON, Passenger.class);
                    passenger.setTown(findTownByName(passengerSeedDtoJSON.getTown()));
                    return passenger;
                })
                .forEach(passengerRepository::save);


        return builder.toString();
    }

    private Town findTownByName(String town) {
        return townRepository.findTownByName(town);
    }

    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {

        StringBuilder builder = new StringBuilder();
        List<Passenger> passengers = passengerRepository.letsTestIt();
        passengers
                .forEach(passenger ->
                        builder.append(String.format("Passenger %s  %s\n" +
                                "\tEmail - %s\n" +
                                "\tPhone - %s\n" +
                                "\tNumber of tickets - %d%n",
                                passenger.getFirstName(),passenger.getLastName(),
                                passenger.getEmail(),passenger.getPhoneNumber(),
                                passenger.getTickets().size())).append(System.lineSeparator()));


        return builder.toString();
    }
}
