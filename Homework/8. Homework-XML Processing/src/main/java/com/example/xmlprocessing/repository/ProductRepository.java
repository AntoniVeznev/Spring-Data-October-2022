package com.example.xmlprocessing.repository;

import com.example.xmlprocessing.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findAllByPriceBetweenAndBuyerIsNullOrderByPrice(BigDecimal lower, BigDecimal upper);

}
