package softuni.exam.models.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cars")
public class Car extends BaseEntity {

    private CarTypeEnum carType;
    private String carMake;
    private String carModel;
    private int year;
    private String plateNumber;
    private int kilometers;
    private double engine;


    public Car() {
    }


    @Enumerated(EnumType.STRING)
    @Column(name = "car_type")
    public CarTypeEnum getCarType() {
        return carType;
    }

    public void setCarType(CarTypeEnum carType) {
        this.carType = carType;
    }
    @Column(name = "car_make")
    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }
    @Column(name = "car_model")
    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }
    @Column(name = "year")
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    @Column(name = "plate_number",unique = true)
    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }
    @Column(name = "kilometers")
    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }
    @Column(name = "engine")
    public double getEngine() {
        return engine;
    }

    public void setEngine(double engine) {
        this.engine = engine;
    }
}
