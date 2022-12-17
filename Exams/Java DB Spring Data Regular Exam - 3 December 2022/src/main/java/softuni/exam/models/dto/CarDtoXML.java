package softuni.exam.models.dto;

import org.hibernate.validator.constraints.Length;
import softuni.exam.models.entity.CarTypeEnum;

import javax.persistence.Enumerated;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "car")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarDtoXML {
    @XmlElement(name = "carMake")
    private String carMake;
    @XmlElement(name = "carModel")
    private String carModel;
    @XmlElement(name = "year")
    private int year;
    @XmlElement(name = "plateNumber")
    private String plateNumber;
    @XmlElement(name = "kilometers")
    private int kilometers;
    @XmlElement(name = "engine")
    private double engine;
    @XmlElement(name = "carType")
    private CarTypeEnum carType;

    public CarDtoXML() {
    }

    @Length(min = 2, max = 30)
    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    @Length(min = 2, max = 30)
    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    @Positive
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Length(min = 2, max = 30)
    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    @Positive
    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }

    @DecimalMin("1.00")
    public double getEngine() {
        return engine;
    }

    public void setEngine(double engine) {
        this.engine = engine;
    }
    @Enumerated

    public CarTypeEnum getCarType() {
        return carType;
    }

    public void setCarType(CarTypeEnum carType) {
        this.carType = carType;
    }
}
