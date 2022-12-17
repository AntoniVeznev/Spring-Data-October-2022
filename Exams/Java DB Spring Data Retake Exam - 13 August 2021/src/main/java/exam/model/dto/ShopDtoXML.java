package exam.model.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "shop")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShopDtoXML {

    @XmlElement(name = "address")
    private String address;
    @XmlElement(name = "employee-count")
    private int employeeCount;
    @XmlElement(name = "income")
    private BigDecimal income;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "shop-area")
    private int shopArea;
    @XmlElement
    private TownNameDtoXML town;

    public ShopDtoXML() {
    }
    @Length(min = 4)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    @Min(1)
    @Max(50)
    public int getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(int employeeCount) {
        this.employeeCount = employeeCount;
    }
    @DecimalMin("20000")
    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }
    @Length(min = 4)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Min(150)
    public int getShopArea() {
        return shopArea;
    }

    public void setShopArea(int shopArea) {
        this.shopArea = shopArea;
    }

    public TownNameDtoXML getTown() {
        return town;
    }

    public void setTown(TownNameDtoXML town) {
        this.town = town;
    }
}
