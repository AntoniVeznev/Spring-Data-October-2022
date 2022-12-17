package com.example.exercise_7_jsonprocessing.service.Impl;

import com.example.exercise_7_jsonprocessing.constants.GlobalConstants;
import com.example.exercise_7_jsonprocessing.module.dto.ProductNameAndPriceDto;
import com.example.exercise_7_jsonprocessing.module.dto.ProductSeedDto;
import com.example.exercise_7_jsonprocessing.module.entity.Product;
import com.example.exercise_7_jsonprocessing.repository.ProductRepository;
import com.example.exercise_7_jsonprocessing.service.CategoryService;
import com.example.exercise_7_jsonprocessing.service.ProductService;
import com.example.exercise_7_jsonprocessing.service.UserService;
import com.example.exercise_7_jsonprocessing.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final static String PRODUCT_FILE_NAME = "products.json";
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final UserService userService;
    private final CategoryService categoryService;

    public ProductServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson, ProductRepository productRepository, UserService userService, CategoryService categoryService) {
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.productRepository = productRepository;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Override
    public void seedProducts() throws IOException {
        if (productRepository.count() > 0) {
            return;
        }
        String fileContent = Files.readString(Path.of(GlobalConstants.RESOURCES_FILE_PATH + PRODUCT_FILE_NAME));

        ProductSeedDto[] productSeedDtos = gson.fromJson(fileContent, ProductSeedDto[].class);

        Arrays.stream(productSeedDtos).filter(validationUtil::isValid)
                .map(productSeedDto -> {
                    Product product = modelMapper.map(productSeedDto, Product.class);
                    product.setSeller(userService.findRandomUser());
                    if (product.getPrice().compareTo(BigDecimal.valueOf(900L)) > 0) {
                        product.setBuyer(userService.findRandomUser());
                    }
                    product.setCategories(categoryService.findRandomCategories());
                    return product;
                })
                .forEach(productRepository::save);
    }

    @Override
    public List<ProductNameAndPriceDto> findAllProductsInRangeOrderByPrice(BigDecimal lower, BigDecimal upper) {
        return productRepository.findAllByPriceBetweenAndBuyerIsNullOrderByPriceDesc(lower, upper)
                .stream()
                .map(product -> {
                    ProductNameAndPriceDto productNameAndPriceDto =
                            modelMapper.map(product, ProductNameAndPriceDto.class);
                    productNameAndPriceDto.setSeller(String.format("%s %s",
                            product.getSeller().getFirstName(),
                            product.getSeller().getLastName()));
                    return productNameAndPriceDto;
                }).collect(Collectors.toList());


    }
}
