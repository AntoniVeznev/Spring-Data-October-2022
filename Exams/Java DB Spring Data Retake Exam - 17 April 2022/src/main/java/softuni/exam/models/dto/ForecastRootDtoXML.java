package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "forecasts")
@XmlAccessorType(XmlAccessType.FIELD)
public class ForecastRootDtoXML {

    @XmlElement(name = "forecast")
    private List<ForecastDtoXML> forecasts;

    public ForecastRootDtoXML() {
    }

    public List<ForecastDtoXML> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<ForecastDtoXML> forecasts) {
        this.forecasts = forecasts;
    }
}
