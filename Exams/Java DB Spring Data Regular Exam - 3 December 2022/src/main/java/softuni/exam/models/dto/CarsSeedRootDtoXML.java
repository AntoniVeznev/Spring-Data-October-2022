package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "cars")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarsSeedRootDtoXML {

    @XmlElement(name = "car")
    private List<CarDtoXML> cars;

    public CarsSeedRootDtoXML() {
    }

    public List<CarDtoXML> getCars() {
        return cars;
    }

    public void setCars(List<CarDtoXML> cars) {
        this.cars = cars;
    }
}
