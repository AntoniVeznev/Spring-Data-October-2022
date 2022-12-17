package exam.model.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "laptops")
public class Laptop extends BaseEntity {

    private String macAddress;
    private double cpuSpeed;
    private int ram;
    private int storage;
    private String description;
    private BigDecimal price;
    private WarrantyEnum warrantyType;

    private Shop shop;

    public Laptop() {
    }
    @OneToOne
    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @Column(name = "mac_address", unique = true)
    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @Column(name = "cpu_speed")
    public double getCpuSpeed() {
        return cpuSpeed;
    }

    public void setCpuSpeed(double cpuSpeed) {
        this.cpuSpeed = cpuSpeed;
    }

    @Column(name = "ram")
    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    @Column(name = "storage")
    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    @Column(name = "description", columnDefinition = "text")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Column(name = "warranty_type")
    @Enumerated(EnumType.ORDINAL)
    public WarrantyEnum getWarrantyType() {
        return warrantyType;
    }

    public void setWarrantyType(WarrantyEnum warrantyType) {
        this.warrantyType = warrantyType;
    }
}
