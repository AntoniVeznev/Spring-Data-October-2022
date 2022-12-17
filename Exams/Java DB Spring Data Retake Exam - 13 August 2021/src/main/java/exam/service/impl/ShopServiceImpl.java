package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dto.ShopSeedRootDtoXML;
import exam.model.entity.Shop;
import exam.model.entity.Town;
import exam.repository.ShopRepository;
import exam.repository.TownRepository;
import exam.service.ShopService;
import exam.util.ValidationUtil;
import exam.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ShopServiceImpl implements ShopService {

    private static final String SHOP_PATH_FILE = "src/main/resources/files/xml/shops.xml";

    private ShopRepository shopRepository;
    private TownRepository townRepository;
    private ValidationUtil validationUtil;
    private ModelMapper modelMapper;
    private Gson gson;
    private XmlParser xmlParser;

    public ShopServiceImpl(ShopRepository shopRepository, TownRepository townRepository, ValidationUtil validationUtil, ModelMapper modelMapper, Gson gson, XmlParser xmlParser) {
        this.shopRepository = shopRepository;
        this.townRepository = townRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return shopRepository.count() > 0;
    }

    @Override
    public String readShopsFileContent() throws IOException {
        return Files.readString(Path.of(SHOP_PATH_FILE));

    }

    @Override
    public String importShops() throws JAXBException, FileNotFoundException {
        StringBuilder builder = new StringBuilder();

        xmlParser.fromFile(SHOP_PATH_FILE, ShopSeedRootDtoXML.class)
                .getShop()
                .stream()
                .filter(shopDtoXML -> {
                    boolean isValid = validationUtil.isValid(shopDtoXML)
                            && !ifShopNameExist(shopDtoXML.getName());
                    if (isValid) {
                        builder
                                .append(String.format("Successfully imported Shop %s - %f%n",
                                        shopDtoXML.getName(),
                                        shopDtoXML.getIncome()));
                    } else {
                        builder.append("Invalid shop").append(System.lineSeparator());
                    }
                    return isValid;
                })
                .map(shopDtoXML -> {

                            Shop shop = modelMapper.map(shopDtoXML, Shop.class);
                            shop.setTown(findTownByTownName(shopDtoXML.getTown().getName()));
                            return shop;
                        }
                ).forEach(shopRepository::save);
        return builder.toString();
    }

    private Town findTownByTownName(String name) {
        return townRepository.findTownByName(name);
    }

    private boolean ifShopNameExist(String name) {
        return shopRepository.existsShopByName(name);
    }
}
