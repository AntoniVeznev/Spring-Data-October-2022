package exam.model.dto;

import com.google.gson.annotations.Expose;
import exam.model.entity.WarrantyEnum;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class LaptopSeedRootDtoJSON {

    @Expose
    private String macAddress;
    @Expose
    private double cpuSpeed;
    @Expose
    private int ram;
    @Expose
    private int storage;
    @Expose
    private String description;
    @Expose
    private BigDecimal price;
    @Expose
    private WarrantyEnum warrantyType;
    @Expose
    private Object shop;

    public LaptopSeedRootDtoJSON() {
    }

    @Length(min = 8)
    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @Positive
    public double getCpuSpeed() {
        return cpuSpeed;
    }

    public void setCpuSpeed(double cpuSpeed) {
        this.cpuSpeed = cpuSpeed;
    }

    @Min(8)
    @Max(128)
    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    @Min(128)
    @Max(1024)
    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    @Length(min = 10)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Positive
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    public WarrantyEnum getWarrantyType() {
        return warrantyType;
    }

    public void setWarrantyType(WarrantyEnum warrantyType) {
        this.warrantyType = warrantyType;
    }

    public Object getShop() {
        return shop;
    }

    public void setShop(Object shop) {
        this.shop = shop;
    }
}
