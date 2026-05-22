package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService
{

    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        updateProductFromRequest(product, productRequest);
        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }

    @Override
    public Optional<ProductResponse> updateProduct(long id, ProductRequest productRequest) {
        return productRepository.findById(id).map(existingProduct -> {
            updateProductFromRequest(existingProduct, productRequest);
            Product savedProduct = productRepository.save(existingProduct);
            return mapToProductResponse(savedProduct);
        });
    }

    @Override
    public List<ProductResponse> getAllProduct()
    {
        return productRepository.findByActiveTrue().stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    @Override
    public Boolean deleteProduct(long id)
    {
        return productRepository.findById(id).map(product -> {
            product.setActive(false);
            productRepository.save(product);
            return true;
        }).orElse(false);
    }

    @Override
    public List<ProductResponse> searchProducts(String keyword)
    {
        List<ProductResponse> data =  productRepository.searchProducts(keyword).stream().map(this::mapToProductResponse).collect(Collectors.toList());
        System.out.println(data);
        return data;
    }

    private ProductResponse mapToProductResponse(Product savedProduct)
    {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(savedProduct.getId());
        productResponse.setName(savedProduct.getName());
        productResponse.setPrice(savedProduct.getPrice());
        productResponse.setDescription(savedProduct.getDescription());
        productResponse.setCategory(savedProduct.getCategory());
        productResponse.setActive(savedProduct.getActive());
        productResponse.setStockQuantity(savedProduct.getStockQuantity());
        productResponse.setImageUrl(savedProduct.getImageUrl());
        return productResponse;
    }

    private void updateProductFromRequest(Product product, ProductRequest productRequest)
    {
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());
        product.setCategory(productRequest.getCategory());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setImageUrl(productRequest.getImageUrl());
    }


}
