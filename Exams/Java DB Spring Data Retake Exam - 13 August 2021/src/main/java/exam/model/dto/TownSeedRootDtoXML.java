package exam.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "towns")
@XmlAccessorType(XmlAccessType.FIELD)
public class TownSeedRootDtoXML {

    @XmlElement(name = "town")
    private List<TownDtoXML> town;

    public TownSeedRootDtoXML() {
    }

    public List<TownDtoXML> getTown() {
        return town;
    }

    public void setTown(List<TownDtoXML> town) {
        this.town = town;
    }
}
