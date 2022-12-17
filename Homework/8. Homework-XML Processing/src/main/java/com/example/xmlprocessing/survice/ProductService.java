package com.example.xmlprocessing.survice;

import com.example.xmlprocessing.model.dto.ProductSeedDto;
import com.example.xmlprocessing.model.dto.ProductViewRootDto;

import java.util.List;

public interface ProductService {

    Long getProductCount();

    void seedProducts(List<ProductSeedDto> products);


    ProductViewRootDto findProductInRangeWithNoBuyer();

}
