package com.demo.backend.controller;

import com.demo.backend.dto.ApiResponse;
import com.demo.backend.repository.ArticleRepository;
import com.demo.backend.repository.CategoryRepository;
import com.demo.backend.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;

    public DashboardController(UserRepository userRepository,
                               ArticleRepository articleRepository,
                               CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/stats")
    public ApiResponse<Map<String, Long>> stats() {
        return ApiResponse.success(Map.of(
                "userCount", userRepository.count(),
                "articleCount", articleRepository.count(),
                "categoryCount", categoryRepository.count()
        ));
    }
}