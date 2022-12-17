package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TicketsRootSeedDtoXML;
import softuni.exam.models.entity.Passenger;
import softuni.exam.models.entity.Plane;
import softuni.exam.models.entity.Ticket;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.repository.TicketRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TicketService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TicketServiceImpl implements TicketService {
    private final static String TICKETS_PATH_FILE = "src/main/resources/files/xml/tickets.xml";
    private TicketRepository ticketRepository;
    private PassengerRepository passengerRepository;
    private PlaneRepository planeRepository;
    private TownRepository townRepository;
    private ModelMapper modelMapper;
    private ValidationUtil validationUtil;
    private XmlParser xmlParser;

    public TicketServiceImpl(TicketRepository ticketRepository, PassengerRepository passengerRepository, PlaneRepository planeRepository, TownRepository townRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.ticketRepository = ticketRepository;
        this.passengerRepository = passengerRepository;
        this.planeRepository = planeRepository;
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }


    @Override
    public boolean areImported() {
        return ticketRepository.count() > 0;
    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return Files.readString(Path.of(TICKETS_PATH_FILE));
    }

    @Override
    public String importTickets() throws JAXBException, FileNotFoundException {
        StringBuilder builder = new StringBuilder();
        xmlParser.fromFile(TICKETS_PATH_FILE, TicketsRootSeedDtoXML.class)
                .getTicket()
                .stream()
                .filter(ticketDto -> {
                    boolean isValid = validationUtil.isValid(ticketDto);
                    if (isValid) {
                        builder
                                .append(String.format("Successfully imported Ticket %s - %s%n",
                                        ticketDto.getFromTown().getName(),
                                        ticketDto.getToTown().getName()));
                    } else {
                        builder.append("Invalid Ticket").append(System.lineSeparator());
                    }
                    return isValid;
                })
                .map(ticketDto -> {
                    Ticket ticket = modelMapper.map(ticketDto, Ticket.class);
                    ticket.setFromTown(findTownWithName(ticket.getFromTown().getName()));
                    ticket.setPassenger(findPassengerWithEmail(ticket.getPassenger().getEmail()));
                    ticket.setPlane(findPlaneWithNumber(ticket.getPlane().getRegisterNumber()));
                    ticket.setToTown(findTownWithName(ticket.getToTown().getName()));
                    return ticket;
                })
                .forEach(ticketRepository::save);

        return builder.toString();
    }

    private Plane findPlaneWithNumber(String registerNumber) {
        return planeRepository.findByRegisterNumber(registerNumber);
    }

    private Passenger findPassengerWithEmail(String email) {
        return passengerRepository.findByEmail(email);
    }

    private Town findTownWithName(String name) {
        return townRepository.findTownByName(name);
    }
}
