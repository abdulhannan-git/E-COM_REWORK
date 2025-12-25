package com.ecommerce.project.service;

import com.ecommerce.project.dto.ProductRequest;
import com.ecommerce.project.dto.ProductResponse;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void mapProductRequestToProduct(Product product, ProductRequest productRequest) {
        //product.setId(productRequest.getId());
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setCategory(productRequest.getCategory());
        product.setImageUrl(productRequest.getImageUrl());
    }

    public ProductResponse mapProductToProductResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setPrice(product.getPrice());
        productResponse.setStockQuantity(product.getStockQuantity());
        productResponse.setImageUrl(product.getImageUrl());
        productResponse.setCategory(product.getCategory());
        productResponse.setActive(product.getActive());
        return productResponse;
    }


    public ProductResponse addProduct(ProductRequest productRequest) {
        Product product = new Product();
        mapProductRequestToProduct(product, productRequest);
        Product productSaved = productRepository.save(product);
        return mapProductToProductResponse(productSaved);
    }

    public List<ProductResponse> fetchAllProducts() {
        return productRepository.findByActiveTrue().stream().map(this::mapProductToProductResponse).collect(Collectors.toList());
    }

    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id).map(
                product -> {
                    mapProductRequestToProduct(product, productRequest);
                    Product saved = productRepository.save(product);
                    return mapProductToProductResponse(saved);
                }
        );
    }


    public List<ProductResponse> searchProduct(String keyword) {
        return productRepository.searchProducts(keyword).stream().map(this::mapProductToProductResponse).collect(Collectors.toList());
    }

    public boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setActive(false);
                    productRepository.save(product);
                    return true;
                }).orElse(false);
    }
}
