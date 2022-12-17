package softuni.exam.models.entity;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalTime;

@Entity
@Table(name = "forecasts")
public class Forecast extends BaseEntity {
    private DayOfWeekEnums dayOfWeek;
    private Double maxTemperature;
    private Double minTemperature;
    private LocalTime sunrise;
    private LocalTime sunset;
    private City city;

    public Forecast() {
    }
    @ManyToOne()
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    public DayOfWeekEnums getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeekEnums dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Column(name = "max_temperature", nullable = false)
    public Double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(Double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }
    @Column(name = "min_temperature", nullable = false)
    public Double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(Double minTemperature) {
        this.minTemperature = minTemperature;
    }
    @Column(name = "sunrise",nullable = false)
    public LocalTime getSunrise() {
        return sunrise;
    }

    public void setSunrise(LocalTime sunrise) {
        this.sunrise = sunrise;
    }
    @Column(name = "sunset",nullable = false)
    public LocalTime getSunset() {
        return sunset;
    }

    public void setSunset(LocalTime sunset) {
        this.sunset = sunset;
    }


}
