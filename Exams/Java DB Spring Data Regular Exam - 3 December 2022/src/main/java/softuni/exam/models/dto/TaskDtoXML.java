package softuni.exam.models.dto;

import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "task")
@XmlAccessorType(XmlAccessType.FIELD)
public class TaskDtoXML {
    @XmlElement(name = "date")
    private String date;
    @XmlElement(name = "price")
    private BigDecimal price;
    @XmlElement(name = "car")
    private CarIdDtoXML car;
    @XmlElement(name = "mechanic")
    private MechanicFirstNameDtoXML mechanic;
    @XmlElement(name = "part")
    private PartIdDtoXml part;

    public TaskDtoXML() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    @Positive
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public CarIdDtoXML getCar() {
        return car;
    }

    public void setCar(CarIdDtoXML car) {
        this.car = car;
    }

    public MechanicFirstNameDtoXML getMechanic() {
        return mechanic;
    }

    public void setMechanic(MechanicFirstNameDtoXML mechanic) {
        this.mechanic = mechanic;
    }

    public PartIdDtoXml getPart() {
        return part;
    }

    public void setPart(PartIdDtoXml part) {
        this.part = part;
    }
}
