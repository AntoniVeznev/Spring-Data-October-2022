package softuni.exam.models.dto;


import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CountryRootDtoJSON {
    @Expose
    private String countryName;
    @Expose
    private String currency;

    public CountryRootDtoJSON() {
    }

    @NotNull
    @Size(min = 2, max = 60)
    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @NotNull
    @Size(min = 2, max = 20)
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
