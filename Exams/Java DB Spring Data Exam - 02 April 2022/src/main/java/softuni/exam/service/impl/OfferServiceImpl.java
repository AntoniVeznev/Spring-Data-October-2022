package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.OfferAgentDtoXML;
import softuni.exam.models.dto.OfferSeedRootDtoXML;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {

    private final static String OFFERS_FILE_PATH = "src/main/resources/files/xml/offers.xml";
    private OfferRepository offerRepository;
    private AgentRepository agentRepository;
    private ModelMapper modelMapper;
    private ValidationUtil validationUtil;
    private Gson gson;
    private XmlParser xmlParser;

    public OfferServiceImpl(OfferRepository offerRepository, AgentRepository agentRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson, XmlParser xmlParser) {
        this.offerRepository = offerRepository;
        this.agentRepository = agentRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of(OFFERS_FILE_PATH));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder builder = new StringBuilder();

        OfferSeedRootDtoXML offerSeedRootDtoXML = xmlParser.fromFile(OFFERS_FILE_PATH, OfferSeedRootDtoXML.class);

        offerSeedRootDtoXML.getOffer()
                .stream()
                .filter(offerDtoXML -> {
                    boolean isValid = validationUtil.isValid(offerDtoXML)
                            && ifAgentWithThatNameEx(offerDtoXML.getAgent().getName());
                    if (isValid) {
                        builder
                                .append(String.format("Successfully imported offer %.2f%n",
                                        offerDtoXML.getPrice()));
                    } else {
                        builder
                                .append("Invalid offer").append(System.lineSeparator());
                    }

                    return isValid;
                })
                .map(offerDtoXML -> {
                    Offer offer = modelMapper.map(offerDtoXML, Offer.class);
                    offer.setAgent(findAgentByName(offerDtoXML.getAgent().getName()));
                    return offer;
                })
                .forEach(offerRepository::save);


        return builder.toString();
    }

    private Agent findAgentByName(String name) {
        return agentRepository.findAgentByFirstName(name);
    }

    private boolean ifAgentWithThatNameEx(String name) {
        return agentRepository.existsByFirstName(name);
    }


    @Override
    public String exportOffers() {
        StringBuilder builder = new StringBuilder();
        offerRepository
                .findThem()
                .forEach(offer -> {
                    builder
                            .append(String.format("    Agent %s %s with offer №%d:\n" +
                                    "   \t\t-Apartment area: %.2f\n" +
                                    "   \t\t--Town: %s\n" +
                                    "   \t\t---Price: %.2f$%n",
                                    offer.getAgent().getFirstName(),
                                    offer.getAgent().getLastName(),
                                    offer.getId(),
                                    offer.getApartment().getArea(),
                                    offer.getApartment().getTown().getTownName(),
                                    offer.getPrice()));

                });

        return builder.toString();
    }
}
