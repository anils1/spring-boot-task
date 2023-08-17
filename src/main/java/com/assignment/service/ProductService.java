package com.assignment.service;

import com.assignment.entities.Product;
import com.assignment.model.ProductSearchCriteria;
import com.assignment.request.ProductRequest;
import com.assignment.response.ApiResponse;
import com.assignment.response.ProductResponse;

import java.util.List;

public interface ProductService {
    List<Product> getAllActiveProducts();

    ProductResponse createProduct(ProductRequest product);

    List<ProductResponse> searchProducts(ProductSearchCriteria criteria);

    ProductResponse updateProduct(ProductRequest product, Long id);

    ApiResponse deleteProductById(Long id);
}
