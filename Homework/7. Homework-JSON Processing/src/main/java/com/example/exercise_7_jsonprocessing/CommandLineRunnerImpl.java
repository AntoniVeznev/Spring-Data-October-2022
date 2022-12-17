package com.example.exercise_7_jsonprocessing;

import com.example.exercise_7_jsonprocessing.module.dto.CategoriesWithProductsDto;
import com.example.exercise_7_jsonprocessing.module.dto.ProductNameAndPriceDto;
import com.example.exercise_7_jsonprocessing.module.dto.UserSoldDto;
import com.example.exercise_7_jsonprocessing.module.entity.Category;
import com.example.exercise_7_jsonprocessing.service.CategoryService;
import com.example.exercise_7_jsonprocessing.service.Impl.CategoryServiceImpl;
import com.example.exercise_7_jsonprocessing.service.ProductService;
import com.example.exercise_7_jsonprocessing.service.UserService;
import com.google.gson.Gson;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
    private static final String OUTPUT_FILES_PATH = "src/main/resources/files/out/";
    private static final String PRODUCT_IN_RANGE_FILE_NAME = "products-in-range.json";
    private static final String SOLD_PRODUCTS_FILE_NAME = "users-sold-products.json";
    private static final String CATEGORY_FILE_NAME = "categories-by-products.json";
    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final BufferedReader bufferedReader;
    private final Gson gson;

    public CommandLineRunnerImpl(CategoryService categoryService, UserService userService, ProductService productService, Gson gson) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.gson = gson;
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();
        System.out.println("Please enter EXERCISE number:");
        int inputNumber = Integer.parseInt(bufferedReader.readLine());

        switch (inputNumber) {
            case 1 -> productsInRange();
            case 2 -> soldProducts();
            case 3 -> newTestTask3();
        }


    }

    private void newTestTask3() throws IOException {
        List<CategoriesWithProductsDto> categoriesWithProductsDtos =
                categoryService.findTEST();
        String content = gson.toJson(categoriesWithProductsDtos);
        writeToFile(OUTPUT_FILES_PATH+CATEGORY_FILE_NAME,content);
    }


    private void soldProducts() throws IOException {
        List<UserSoldDto> userSoldDtos =
                userService.findAllUsersWithMoreThanOneSoldProducts();
        String content = gson.toJson(userSoldDtos);
        writeToFile(OUTPUT_FILES_PATH + SOLD_PRODUCTS_FILE_NAME, content);


    }

    private void productsInRange() throws IOException {
        List<ProductNameAndPriceDto> productDtos =
                productService.
                        findAllProductsInRangeOrderByPrice(BigDecimal.valueOf(500L), BigDecimal.valueOf(1000L));
        String content = gson.toJson(productDtos);
        writeToFile(OUTPUT_FILES_PATH + PRODUCT_IN_RANGE_FILE_NAME, content);
    }

    private void writeToFile(String filePath, String content) throws IOException {
        Files.write(Path.of(filePath), Collections.singleton(content));
    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        userService.seedUsers();
        productService.seedProducts();
    }
}
