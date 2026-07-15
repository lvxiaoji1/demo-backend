package com.demo.backend.service;

import com.demo.backend.dto.ArticleCreateDTO;
import com.demo.backend.dto.ArticleUpdateDTO;
import com.demo.backend.entity.Article;
import com.demo.backend.entity.Category;
import com.demo.backend.entity.User;
import com.demo.backend.exception.BusinessException;
import com.demo.backend.repository.ArticleRepository;
import com.demo.backend.repository.CategoryRepository;
import com.demo.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ArticleService(ArticleRepository articleRepository,
                          UserRepository userRepository,
                          CategoryRepository categoryRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public Article create(ArticleCreateDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new BusinessException(404, "user not found: " + dto.getUserId()));
        Article article = new Article();
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setUser(user);

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new BusinessException(404, "category not found: " + dto.getCategoryId()));
            article.setCategory(category);
        }

        return articleRepository.save(article);
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Page<Article> findAll(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "article not found: " + id));
    }

    public List<Article> findByUserId(Long userId) {
        return articleRepository.findByUserId(userId);
    }

    public Page<Article> search(String keyword, Pageable pageable) {
        return articleRepository.search(keyword, pageable);
    }

    public Article update(Long id, ArticleUpdateDTO dto) {
        Article existing = findById(id);
        existing.setTitle(dto.getTitle());
        existing.setContent(dto.getContent());
        return articleRepository.save(existing);
    }

    public void delete(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new BusinessException(404, "article not found: " + id);
        }
        articleRepository.deleteById(id);
    }
}