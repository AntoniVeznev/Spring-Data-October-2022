package com.example.xmlprocessing.survice.Impl;

import com.example.xmlprocessing.model.dto.ProductSeedDto;
import com.example.xmlprocessing.model.dto.ProductViewRootDto;
import com.example.xmlprocessing.model.dto.ProductWithSellerDto;
import com.example.xmlprocessing.model.entity.Product;
import com.example.xmlprocessing.repository.ProductRepository;
import com.example.xmlprocessing.repository.UserRepository;
import com.example.xmlprocessing.survice.CategoryService;
import com.example.xmlprocessing.survice.ProductService;
import com.example.xmlprocessing.survice.UserService;
import com.example.xmlprocessing.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final UserService userService;

    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository, CategoryService categoryService, ModelMapper modelMapper, ValidationUtil validationUtil, UserService userService) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.userService = userService;
    }

    @Override
    public Long getProductCount() {
        return productRepository.count();
    }

    @Override
    public void seedProducts(List<ProductSeedDto> products) {
        products
                .stream()
                .filter(validationUtil::isValid)
                .map(productSeedDto -> {
                    Product product = modelMapper.map(productSeedDto, Product.class);
                    product.setSeller(userService.getRandomUser());
                    if (product.getPrice().compareTo(BigDecimal.valueOf(700L)) > 0) {
                        product.setBuyer(userService.getRandomUser());
                    }
                    product.setCategories(categoryService.getRandomCategories());
                    return product;
                }).forEach(productRepository::save);
    }

    @Override
    public ProductViewRootDto findProductInRangeWithNoBuyer() {
        ProductViewRootDto rootDto = new ProductViewRootDto();
        rootDto
                .setProducts(productRepository
                        .findAllByPriceBetweenAndBuyerIsNullOrderByPrice(BigDecimal.valueOf(500L), BigDecimal.valueOf(1000L))
                        .stream()
                        .map(product -> {
                            ProductWithSellerDto productWithSellerDto = modelMapper
                                    .map(product, ProductWithSellerDto.class);
                            if (product.getSeller().getFirstName()==null){
                                product.getSeller().setFirstName("");
                            }
                            productWithSellerDto.setSeller(String.format("%s %s"
                                    , product.getSeller().getFirstName()
                                    , product.getSeller().getLastName()));
                            return productWithSellerDto;
                        }).collect(Collectors.toList()));

        return rootDto;
    }
}
