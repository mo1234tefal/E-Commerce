package com.mo.ecommerce.service;

import com.mo.ecommerce.dto.category.CategoryResponseDto;
import com.mo.ecommerce.entity.Category;
import com.mo.ecommerce.repository.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepo categoryRepo;

    public CategoryResponseDto addCategory(String categoryName){
        var category = categoryRepo.findByNameIgnoreCase(categoryName);
        if(category.isPresent()){
            throw new RuntimeException("this category already exists");
        }
        Category newCategory = Category.builder().name(categoryName).build();
        categoryRepo.save(newCategory);
        return CategoryResponseDto.builder().name(newCategory.getName()).id(newCategory.getId()).build();

    }

    public String deleteCategory(String categoryName){
        Category category = categoryRepo.findByNameIgnoreCase(categoryName).orElseThrow(()-> new RuntimeException(("this category name is not found !")));
        categoryRepo.delete(category);
        return categoryName + " is deleted";
    }

    public CategoryResponseDto updateCategory(String categoryName){
        Category category = categoryRepo.findByNameIgnoreCase(categoryName).orElseThrow(()-> new RuntimeException("this category is not found"));
        category.setName(categoryName);
        categoryRepo.save(category);
        return CategoryResponseDto.builder().name(category.getName()).id(category.getId()).build();
    }

    public CategoryResponseDto getCategory(String categoryName){
        Category category = categoryRepo.findByNameIgnoreCase(categoryName).orElseThrow(()-> new RuntimeException("this category is not found"));
        return CategoryResponseDto.builder().id(category.getId()).name(category.getName()).build();
    }

    public List<CategoryResponseDto> getAllCategories(){
        return categoryRepo.findAll().stream()
                .map(cat-> {
                    return CategoryResponseDto.builder().id(cat.getId()).name(cat.getName()).build();
                }).collect(Collectors.toList());
    }

}
