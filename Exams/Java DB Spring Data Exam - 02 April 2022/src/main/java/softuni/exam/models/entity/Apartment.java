package softuni.exam.models.entity;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "apartments")
public class Apartment extends BaseEntity {

    private ApartmentEnum apartmentType;
    private Double area;

    private Town town;
    private List<Offer> offers;

    public Apartment() {
    }

    @OneToMany(mappedBy = "apartment",fetch = FetchType.EAGER)
    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    @OneToOne
    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "apartment_type")
    public ApartmentEnum getApartmentType() {
        return apartmentType;
    }

    public void setApartmentType(ApartmentEnum apartmentType) {
        this.apartmentType = apartmentType;
    }

    @Column(name = "area")
    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }
}
