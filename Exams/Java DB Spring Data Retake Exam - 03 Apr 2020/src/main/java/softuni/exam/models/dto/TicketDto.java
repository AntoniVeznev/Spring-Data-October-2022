package softuni.exam.models.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "ticket")
@XmlAccessorType(XmlAccessType.FIELD)
public class TicketDto {

    @XmlElement(name = "serial-number")
    private String serialNumber;
    @XmlElement(name = "price")
    private BigDecimal price;
    @XmlElement(name = "take-off")
    private String takeoff;

    @XmlElement(name = "from-town")
    private FromTownDto fromTown;
    @XmlElement(name = "to-town")
    private ToTownDto toTown;
    @XmlElement(name = "passenger")
    private PassengerDto passenger;
    @XmlElement(name = "plane")
    private PlaneDto plane;

    public TicketDto() {
    }
    @Length(min = 2)
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    @Positive
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTakeoff() {
        return takeoff;
    }

    public void setTakeoff(String takeoff) {
        this.takeoff = takeoff;
    }

    public FromTownDto getFromTown() {
        return fromTown;
    }

    public void setFromTown(FromTownDto fromTown) {
        this.fromTown = fromTown;
    }

    public ToTownDto getToTown() {
        return toTown;
    }

    public void setToTown(ToTownDto toTown) {
        this.toTown = toTown;
    }

    public PassengerDto getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerDto passenger) {
        this.passenger = passenger;
    }

    public PlaneDto getPlane() {
        return plane;
    }

    public void setPlane(PlaneDto plane) {
        this.plane = plane;
    }
}
