package com.example.football.models.dto;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "stats")
@XmlAccessorType(XmlAccessType.FIELD)
public class StatsRootSeedDtoXML {

    @XmlElement(name = "stat")
    private List<StatsDtoXML> stat;

    public StatsRootSeedDtoXML() {
    }

    public List<StatsDtoXML> getStat() {
        return stat;
    }

    public void setStat(List<StatsDtoXML> stat) {
        this.stat = stat;
    }
}
