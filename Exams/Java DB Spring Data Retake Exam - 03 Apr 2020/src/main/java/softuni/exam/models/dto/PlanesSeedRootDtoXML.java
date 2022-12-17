package softuni.exam.models.dto;


import javax.validation.constraints.DecimalMin;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "planes")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlanesSeedRootDtoXML {
    @XmlElement(name = "plane")
    private List<PlaneDtoXml> plane;

    public PlanesSeedRootDtoXML() {
    }
@DecimalMin("40.00")
    public List<PlaneDtoXml> getPlanes() {
        return plane;
    }

    public void setPlanes(List<PlaneDtoXml> plane) {
        this.plane = plane;
    }
}
