package softuni.exam.models.dto;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Positive;

public class PassengerSeedDtoJSON {

    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private int age;
    @Expose
    private String phoneNumber;
    @Expose
    private String email;
    @Expose
    private String town;

    public PassengerSeedDtoJSON() {
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

    @Positive
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Length(min = 2)
    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
