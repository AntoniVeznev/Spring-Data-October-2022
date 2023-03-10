package com.example.football.models.dto;


import com.example.football.models.entity.PositionEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "player")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerSeedDtoXML {
    @XmlElement(name = "first-name")
    private String firstName;
    @XmlElement(name = "last-name")
    private String lastName;
    @XmlElement(name = "email")
    private String email;
    @XmlElement(name = "birth-date")
    private String birthDate;
    @XmlElement(name = "position")
    private PositionEnum position;
    @XmlElement(name = "town")
    private PlayerTownNameDtoXML town;
    @XmlElement(name = "team")
    private PlayerTeamNameDtoXML team;
    @XmlElement(name = "stat")
    private PlayerStatIdDtoXML stat;

    public PlayerSeedDtoXML() {
    }
    @Length(min = 2)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    @Length(min = 2)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public PositionEnum getPosition() {
        return position;
    }

    public void setPosition(PositionEnum position) {
        this.position = position;
    }

    public PlayerTownNameDtoXML getTown() {
        return town;
    }

    public void setTown(PlayerTownNameDtoXML town) {
        this.town = town;
    }

    public PlayerTeamNameDtoXML getTeam() {
        return team;
    }

    public void setTeam(PlayerTeamNameDtoXML team) {
        this.team = team;
    }

    public PlayerStatIdDtoXML getStat() {
        return stat;
    }

    public void setStat(PlayerStatIdDtoXML stat) {
        this.stat = stat;
    }
}
