package exam.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@XmlRootElement(name = "shops")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShopSeedRootDtoXML {
    @XmlElement(name = "shop")
    private List<ShopDtoXML> shop;

    public ShopSeedRootDtoXML() {
    }

    public List<ShopDtoXML> getShop() {
        return shop;
    }

    public void setShop(List<ShopDtoXML> shop) {
        this.shop = shop;
    }
}
