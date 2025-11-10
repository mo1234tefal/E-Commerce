package com.mo.ecommerce.service;

import com.mo.ecommerce.dto.product.ProductRequestDto;
import com.mo.ecommerce.dto.product.ProductResponse;
import com.mo.ecommerce.entity.Category;
import com.mo.ecommerce.entity.Product;
import com.mo.ecommerce.repository.CategoryRepo;
import com.mo.ecommerce.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    public ProductService(ProductRepo productRepo, CategoryRepo categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    public List<ProductResponse> getAllProducts(){
        return productRepo.findAll().stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }

    public ProductResponse getProduct(Long id){
        return mapToDto(productRepo.findById(id).orElseThrow(()-> new RuntimeException("Product not found")));
    }
    public ProductResponse addProduct(ProductRequestDto productRequestDto){
        Product product = mapToProduct(productRequestDto);
        productRepo.save(product);
        return mapToDto(product);
    }

    public String deleteProduct(Long id){
        Product product = productRepo.findById(id).orElseThrow(()-> new RuntimeException("Product not found"));
        productRepo.delete(product);
        return "Product deleted";
    }
    public ProductResponse updateProduct(Long id, ProductRequestDto productRequestDto){
        Product product = productRepo.findById(id).orElseThrow(()-> new RuntimeException("Product not found"));
        Product updatedProduct  = mapToProduct(productRequestDto);
        return mapToDto(updatedProduct);
    }

    public List<ProductResponse> searchProduct(String keyword){
        return productRepo.findByNameContainingIgnoreCase(keyword).stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }

    public List<ProductResponse> getProductsByCategory(Long CategoryId){
        Category category = categoryRepo.findById(CategoryId).orElseThrow(()-> new RuntimeException("Category not found"));
        return productRepo.findByCategory(category).stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }
    public void reduceStock(Long productId  , Integer quantity){
        Product product = productRepo.findById(productId).orElseThrow(()-> new RuntimeException("Product not found"));
        if(product.getStock() < quantity) {
            throw new RuntimeException("Not enough stock available!");
        }
        product.setStock(product.getStock() - quantity);
        productRepo.save(product);
    }
    public boolean isInStock(Long productId) {
        Product product = productRepo.findById(productId).orElseThrow(()-> new RuntimeException("Product not found "));
        return product.getStock() != null && product.getStock() > 0;
    }
    // do not forget do pagination and query by example





    private ProductResponse mapToDto(Product product){
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setPrice(product.getPrice());
        Category category = categoryRepo.findById(product.getCategory().getId()).get();
        productResponse.setCategoryName(category.getName());
        productResponse.setStock(product.getStock());
        productResponse.setImageUrl(product.getImageUrl());
        return productResponse;
    }
    private Product mapToProduct(ProductRequestDto productRequestDto){
        Product product = new Product();
        product.setName(productRequestDto.getName());
        product.setDescription(productRequestDto.getDescription());
        product.setPrice(productRequestDto.getPrice());
        product.setImageUrl(productRequestDto.getImageUrl());
        product.setStock(productRequestDto.getStock());
        Category category = categoryRepo.findById(productRequestDto.getCategoryId()).orElseThrow(()-> new RuntimeException("category not found 'false category id'"));
        product.setCategory(category);
        return product;
    }

}
