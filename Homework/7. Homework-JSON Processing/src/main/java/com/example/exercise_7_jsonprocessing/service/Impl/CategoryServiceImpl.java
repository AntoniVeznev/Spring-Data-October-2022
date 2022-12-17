package com.example.exercise_7_jsonprocessing.service.Impl;

import com.example.exercise_7_jsonprocessing.constants.GlobalConstants;
import com.example.exercise_7_jsonprocessing.module.dto.CategoriesWithProductsDto;
import com.example.exercise_7_jsonprocessing.module.dto.CategorySeedDto;
import com.example.exercise_7_jsonprocessing.module.entity.Category;
import com.example.exercise_7_jsonprocessing.module.entity.Product;
import com.example.exercise_7_jsonprocessing.repository.CategoryRepository;
import com.example.exercise_7_jsonprocessing.service.CategoryService;
import com.example.exercise_7_jsonprocessing.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final String CATEGORIES_FILE_NAME = "categories.json";
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;


    public CategoryServiceImpl(CategoryRepository categoryRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;

    }

    @Override
    public void seedCategories() throws IOException {
        if (categoryRepository.count() > 0) {
            return;
        }
        String fileContent = Files.readString(Path.of(GlobalConstants.RESOURCES_FILE_PATH + CATEGORIES_FILE_NAME));
        CategorySeedDto[] categorySeedDtos = gson.fromJson(fileContent, CategorySeedDto[].class);
        Arrays.stream(categorySeedDtos)
                .filter(validationUtil::isValid)
                .map(categorySeedDto -> modelMapper.map(categorySeedDto, Category.class))
                .forEach(categoryRepository::save);
    }

    @Override
    public Set<Category> findRandomCategories() {
        Set<Category> categorySet = new HashSet<>();
        int catCount = ThreadLocalRandom.current().nextInt(1, 3);
        long totalCategoriesCount = categoryRepository.count();
        for (int i = 0; i < catCount; i++) {
            long randomID = ThreadLocalRandom.current().nextLong(1, totalCategoriesCount + 1);
            categorySet.add(categoryRepository.findById(randomID).orElse(null));
        }
        return categorySet;
    }

    @Override
    public List<CategoriesWithProductsDto> findTEST() {
        return categoryRepository.findLetsTest().stream()
                .map(category -> {
                    CategoriesWithProductsDto categoriesWithProductsDto =
                            modelMapper.map(category, CategoriesWithProductsDto.class);
                    categoriesWithProductsDto.setCategory(category.getName());
                    categoriesWithProductsDto.setProductsCount(category.getProducts().size());
                    categoriesWithProductsDto.setAveragePrice(findAveragePrice(category));
                    categoriesWithProductsDto.setTotalRevenue(findTotalPriceSum(category));
                    return categoriesWithProductsDto;
                }).collect(Collectors.toList());
    }

    private BigDecimal findTotalPriceSum(Category category) {
        Set<Product> products = category.getProducts();
        BigDecimal sum = BigDecimal.valueOf(0);
        for (Product product : products) {
            BigDecimal productPrice = product.getPrice();
            sum = sum.add(productPrice);
        }
        return sum;
    }

    private BigDecimal findAveragePrice(Category category) {
        Set<Product> products = category.getProducts();
        BigDecimal divisor = BigDecimal.valueOf(products.size());
        MathContext mc = new MathContext(6);
        BigDecimal sum = BigDecimal.valueOf(0);
        BigDecimal res = BigDecimal.valueOf(0);
        for (Product product : products) {
            BigDecimal productPrice = product.getPrice();
            sum = sum.add(productPrice);
        }
        return sum.divide(divisor,mc);
    }


}


















