package exam.model.dto;

import com.google.gson.annotations.Expose;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

public class CustomerSeedRootDtoJSON {

    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private String email;
    @Expose
    private String registeredOn;
    @Expose
    private TownCustomerDtoJSON town;

    public CustomerSeedRootDtoJSON() {
    }

    public TownCustomerDtoJSON getTown() {
        return town;
    }

    public void setTown(TownCustomerDtoJSON town) {
        this.town = town;
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

    public String getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(String registeredOn) {
        this.registeredOn = registeredOn;
    }


}
