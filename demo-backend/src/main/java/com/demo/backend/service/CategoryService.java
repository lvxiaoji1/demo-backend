package com.demo.backend.service;

import com.demo.backend.dto.CategoryCreateDTO;
import com.demo.backend.entity.Category;
import com.demo.backend.exception.BusinessException;
import com.demo.backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "category not found: " + id));
    }

    public Category create(CategoryCreateDTO dto) {
        Category c = new Category();
        c.setName(dto.getName());
        c.setDescription(dto.getDescription());
        return categoryRepository.save(c);
    }

    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new BusinessException(404, "category not found: " + id);
        }
        categoryRepository.deleteById(id);
    }
}