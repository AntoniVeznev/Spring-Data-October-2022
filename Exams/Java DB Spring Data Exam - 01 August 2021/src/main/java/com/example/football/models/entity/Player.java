package com.example.football.models.entity;


import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "players")
public class Player extends BaseEntity {

    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private PositionEnum position;

    private Stat stat;
    private Team team;
    private Town town;

    public Player() {
    }

    @OneToOne
    public Stat getStat() {
        return stat;
    }

    public void setStat(Stat stat) {
        this.stat = stat;
    }

    @OneToOne
    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @OneToOne
    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "email", unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "birth_date")
    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "position")
    public PositionEnum getPosition() {
        return position;
    }

    public void setPosition(PositionEnum position) {
        this.position = position;
    }
}
