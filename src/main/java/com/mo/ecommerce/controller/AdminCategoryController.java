package com.mo.ecommerce.controller;

import com.mo.ecommerce.dto.category.CategoryResponseDto;
import com.mo.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController{
    private CategoryService categoryService;

    @PostMapping("/{name}")
    public ResponseEntity<CategoryResponseDto> addCategory(@PathVariable String name){
        return new ResponseEntity<>(categoryService.addCategory(name) , HttpStatus.CREATED);
    }

    @PutMapping("/{name}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable String name ){
        return new ResponseEntity<>(categoryService.updateCategory(name) , HttpStatus.OK);
    }
    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteCategory(@PathVariable String name){
        return new ResponseEntity<>(categoryService.deleteCategory(name) , HttpStatus.OK);
    }
    @GetMapping("/{name}")
    public ResponseEntity<CategoryResponseDto> getCategory(@PathVariable String name){
        return new ResponseEntity<>(categoryService.getCategory(name) , HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories(){
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

}