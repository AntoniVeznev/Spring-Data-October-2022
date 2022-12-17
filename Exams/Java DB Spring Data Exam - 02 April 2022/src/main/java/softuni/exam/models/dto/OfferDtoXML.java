package softuni.exam.models.dto;

import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.List;

@XmlRootElement(name = "apartment")
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferDtoXML {
    @XmlElement(name = "price")
    private BigDecimal price;
    @XmlElement(name = "agent")
    private OfferAgentDtoXML agent;
    @XmlElement(name = "apartment")
    private OfferApartmentDtoXML apartment;

    @XmlElement(name = "publishedOn")
    private String publishedOn;

    public OfferDtoXML() {
    }

    public String getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(String publishedOn) {
        this.publishedOn = publishedOn;
    }

    @Positive
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public OfferAgentDtoXML getAgent() {
        return agent;
    }

    public void setAgent(OfferAgentDtoXML agent) {
        this.agent = agent;
    }

    public OfferApartmentDtoXML getApartment() {
        return apartment;
    }

    public void setApartment(OfferApartmentDtoXML apartment) {
        this.apartment = apartment;
    }
}
