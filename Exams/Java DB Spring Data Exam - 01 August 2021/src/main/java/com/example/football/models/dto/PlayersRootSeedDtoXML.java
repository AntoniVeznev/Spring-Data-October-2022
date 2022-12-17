package com.example.football.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "players")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayersRootSeedDtoXML {

    @XmlElement(name = "player")
    private List<PlayerSeedDtoXML> player;

    public PlayersRootSeedDtoXML() {
    }

    public List<PlayerSeedDtoXML> getPlayer() {
        return player;
    }

    public void setPlayer(List<PlayerSeedDtoXML> player) {
        this.player = player;
    }
}
