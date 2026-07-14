package com.demo.backend.controller;

import com.demo.backend.dto.ApiResponse;
import com.demo.backend.dto.CategoryCreateDTO;
import com.demo.backend.dto.CategoryResponse;
import com.demo.backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ApiResponse<CategoryResponse> create(@Valid @RequestBody CategoryCreateDTO dto) {
        return ApiResponse.success(CategoryResponse.from(categoryService.create(dto)));
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> findAll() {
        List<CategoryResponse> list = categoryService.findAll().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success(list);
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> findById(@PathVariable Long id) {
        return ApiResponse.success(CategoryResponse.from(categoryService.findById(id)));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ApiResponse.success(null);
    }
}