package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dto.LaptopSeedRootDtoJSON;
import exam.model.entity.Laptop;
import exam.model.entity.Shop;
import exam.repository.LaptopRepository;
import exam.repository.ShopRepository;
import exam.service.LaptopService;
import exam.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Service
public class LaptopServiceImpl implements LaptopService {

    private final static String LAPTOP_PATH_FILE = "src/main/resources/files/json/laptops.json";
    private LaptopRepository laptopRepository;
    private ShopRepository shopRepository;
    private ModelMapper modelMapper;
    private ValidationUtil validationUtil;
    private Gson gson;

    public LaptopServiceImpl(LaptopRepository laptopRepository, ShopRepository shopRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.laptopRepository = laptopRepository;
        this.shopRepository = shopRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return laptopRepository.count() > 0;
    }

    @Override
    public String readLaptopsFileContent() throws IOException {
        return Files.readString(Path.of(LAPTOP_PATH_FILE));
    }

    @Override
    public String importLaptops() throws IOException {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(gson.fromJson(readLaptopsFileContent(), LaptopSeedRootDtoJSON[].class))
                .filter(laptopSeedRootDtoJSON -> {
                    boolean isValid = validationUtil.isValid(laptopSeedRootDtoJSON)
                            && !ifMacAddressExist(laptopSeedRootDtoJSON.getMacAddress());
                    if (isValid) {
                        builder
                                .append(String.format("Successfully imported Laptop %s - %.2f - %d - %d%n",
                                        laptopSeedRootDtoJSON.getMacAddress(),
                                        laptopSeedRootDtoJSON.getCpuSpeed(),
                                        laptopSeedRootDtoJSON.getRam(),
                                        laptopSeedRootDtoJSON.getStorage()));
                    } else {
                        builder
                                .append("Invalid Laptop").append(System.lineSeparator());
                    }
                    return isValid;
                })
                .map(laptopSeedRootDtoJSON -> {
                    Laptop laptop = modelMapper.map(laptopSeedRootDtoJSON, Laptop.class);
                    //String name = customerSeedRootDtoJSON.getTown().toString().split("=")[1].replace("}", "");
                    String name = laptopSeedRootDtoJSON.getShop().toString().split("=")[1].replace("}", "");
                    laptop.setShop(findShopByName(name));
                    return laptop;
                })
                .forEach(laptopRepository::save);

        return builder.toString();
    }

    private Shop findShopByName(String name) {
        return shopRepository.findShopByName(name);
    }

    private boolean ifMacAddressExist(String macAddress) {
        return laptopRepository.existsByMacAddress(macAddress);
    }

    @Override
    public String exportBestLaptops() {
        StringBuilder builder = new StringBuilder();
        List<Laptop> laptopsBySomeStuff = laptopRepository.findLaptopsBySomeStuff();
        laptopsBySomeStuff
                .forEach(laptop ->
                        builder.append(String.format("Laptop - %s\n" +
                                        "*Cpu speed - %.2f\n" +
                                        "**Ram - %d\n" +
                                        "***Storage - %d\n" +
                                        "****Price - %.2f\n" +
                                        "#Shop name - %s\n" +
                                        "##Town - %s\n",
                                laptop.getMacAddress(),
                                laptop.getCpuSpeed(),
                                laptop.getRam(),
                                laptop.getStorage(),
                                laptop.getPrice(),
                                laptop.getShop().getName(),
                                laptop.getShop().getTown().getName())
                        ).append(System.lineSeparator())
                );
        return builder.toString();
    }
}
