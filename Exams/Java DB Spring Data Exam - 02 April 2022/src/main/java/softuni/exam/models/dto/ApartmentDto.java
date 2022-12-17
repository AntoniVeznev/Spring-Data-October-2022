package softuni.exam.models.dto;

import softuni.exam.models.entity.ApartmentEnum;

import javax.validation.constraints.DecimalMin;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "apartment")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApartmentDto {

    @XmlElement(name = "apartmentType")
    private ApartmentEnum apartmentType;
    @XmlElement(name = "area")
    private Double area;
    @XmlElement(name = "town")
    private String town;

    public ApartmentDto() {
    }

    public ApartmentEnum getApartmentType() {
        return apartmentType;
    }

    public void setApartmentType(ApartmentEnum apartmentType) {
        this.apartmentType = apartmentType;
    }

    @DecimalMin("40.00")
    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
