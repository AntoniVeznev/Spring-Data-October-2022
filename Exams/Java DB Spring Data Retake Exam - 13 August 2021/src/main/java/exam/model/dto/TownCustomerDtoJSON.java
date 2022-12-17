package exam.model.dto;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

public class TownCustomerDtoJSON {
    @Expose
    private String name;

    public TownCustomerDtoJSON() {
    }
    @Length(min = 2)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
