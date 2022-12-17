package softuni.exam.models.dto;

import com.sun.xml.bind.XmlAccessorFactory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "apartments")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApartmentRootSeedDtoXML {

    @XmlElement(name = "apartment")
    List<ApartmentDto> apartments;

    public ApartmentRootSeedDtoXML() {
    }

    public List<ApartmentDto> getApartments() {
        return apartments;
    }

    public void setApartments(List<ApartmentDto> apartments) {
        this.apartments = apartments;
    }
}
