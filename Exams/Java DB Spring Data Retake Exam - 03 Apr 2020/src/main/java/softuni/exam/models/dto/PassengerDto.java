package softuni.exam.models.dto;


import javax.validation.constraints.Email;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "email")
@XmlAccessorType(XmlAccessType.FIELD)
public class PassengerDto {
    @XmlElement(name = "email")
    private String email;

    public PassengerDto() {
    }

    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
