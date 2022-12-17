package Entity_task_4;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "patients")
public class Patient extends BaseClass {
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private Date dateOfBirth;
    private String pictureURL;
    private boolean information;
    private Set<Visitation> visitations;
    private Set<Diagnose> diagnoses;
    private Set<Medicament> medicaments;

    public Patient() {
    }

    @ManyToMany
    public Set<Visitation> getVisitations() {
        return visitations;
    }

    public void setVisitations(Set<Visitation> visitationTask4s) {
        this.visitations = visitationTask4s;
    }

    @ManyToMany
    public Set<Diagnose> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(Set<Diagnose> diagnoses) {
        this.diagnoses = diagnoses;
    }

    @ManyToMany
    public Set<Medicament> getMedicaments() {
        return medicaments;
    }

    public void setMedicaments(Set<Medicament> medicamentTask4s) {
        this.medicaments = medicamentTask4s;
    }

    @Column(name = "first_name", length = 50, columnDefinition = "TEXT")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name", length = 50, columnDefinition = "TEXT")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "address", length = 200, unique = true, columnDefinition = "TEXT")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "email", length = 50, unique = true, columnDefinition = "TEXT")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "date_of_birth", columnDefinition = "TEXT")
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Column(name = "picture_url", length = 1000, columnDefinition = "TEXT")
    public String getPicture() {
        return pictureURL;
    }

    public void setPicture(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    @Column(name = "medical_insurance_or_not")
    public boolean isInformation() {
        return information;
    }

    public void setInformation(boolean information) {
        this.information = information;
    }
}