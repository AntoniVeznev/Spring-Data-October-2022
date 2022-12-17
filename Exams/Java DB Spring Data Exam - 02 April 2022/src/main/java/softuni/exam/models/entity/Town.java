package softuni.exam.models.entity;

import javax.persistence.*;



@Entity
@Table(name = "towns")
public class Town extends BaseEntity {

    private String townName;

    private Integer population;



    public Town() {
    }


    @Column(name = "town_name", unique = true)
    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    @Column(name = "population")
    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }
}
