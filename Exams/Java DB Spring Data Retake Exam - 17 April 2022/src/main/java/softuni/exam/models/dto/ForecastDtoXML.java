package softuni.exam.models.dto;

import softuni.exam.models.entity.DayOfWeekEnums;

import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "forecast")
@XmlAccessorType(XmlAccessType.FIELD)

public class ForecastDtoXML {
    @XmlElement(name = "day_of_week")
    private DayOfWeekEnums dayOfWeek;
    @XmlElement(name = "max_temperature")
    private Double maxTemperature;
    @XmlElement(name = "min_temperature")
    private Double minTemperature;
    @XmlElement(name = "sunrise")
    private String sunrise;
    @XmlElement(name = "sunset")
    private String sunset;
    @XmlElement(name = "city")
    private Long city;

    public ForecastDtoXML() {
    }

    @NotNull
    public DayOfWeekEnums getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeekEnums dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @NotNull
    @DecimalMin("-20") @DecimalMax("60")
    public Double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(Double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    @NotNull
    @DecimalMin("-50") @DecimalMax("40")
    public Double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(Double minTemperature) {
        this.minTemperature = minTemperature;
    }

    @NotNull
    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    @NotNull
    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public Long getCity() {
        return city;
    }

    public void setCity(Long city) {
        this.city = city;
    }
}
