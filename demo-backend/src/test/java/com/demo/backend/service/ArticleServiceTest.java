package com.demo.backend.service;

import com.demo.backend.dto.ArticleCreateDTO;
import com.demo.backend.entity.Article;
import com.demo.backend.entity.User;
import com.demo.backend.exception.BusinessException;
import com.demo.backend.repository.ArticleRepository;
import com.demo.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock private ArticleRepository articleRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private ArticleService articleService;

    @Test
    void createArticle_shouldSaveAndReturnArticle() {
        User author = new User(); author.setId(1L); author.setName("author");
        ArticleCreateDTO dto = new ArticleCreateDTO();
        dto.setTitle("title"); dto.setContent("content"); dto.setUserId(1L);
        Article saved = new Article();
        saved.setId(1L); saved.setTitle("title");
        saved.setContent("content"); saved.setUser(author);
        when(userRepository.findById(1L)).thenReturn(Optional.of(author));
        when(articleRepository.save(any(Article.class))).thenReturn(saved);
        Article result = articleService.create(dto);
        assertEquals("title", result.getTitle());
        assertEquals("content", result.getContent());
        assertEquals("author", result.getUser().getName());
    }

    @Test
    void createArticle_userNotFound_shouldThrow() {
        ArticleCreateDTO dto = new ArticleCreateDTO();
        dto.setTitle("t"); dto.setContent("c"); dto.setUserId(999L);
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> articleService.create(dto));
    }

    @Test
    void search_shouldReturnResults() {
        Article a = new Article(); a.setId(1L); a.setTitle("Spring Boot Guide");
        Page<Article> page = new PageImpl<>(List.of(a));
        PageRequest pr = PageRequest.of(0, 10);
        when(articleRepository.search("Spring", pr)).thenReturn(page);
        Page<Article> result = articleService.search("Spring", pr);
        assertEquals(1, result.getTotalElements());
        assertEquals("Spring Boot Guide", result.getContent().get(0).getTitle());
    }
}