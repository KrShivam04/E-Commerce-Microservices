package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;

import java.util.List;
import java.util.Optional;

public interface ProductService
{
    ProductResponse createProduct(ProductRequest productRequest);

    Optional<ProductResponse> updateProduct(long id, ProductRequest productRequest);

    List<ProductResponse> getAllProduct();

    Boolean deleteProduct(long id);

    List<ProductResponse> searchProducts(String keyword);
}
