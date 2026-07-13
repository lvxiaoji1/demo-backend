package com.demo.backend.controller;

import com.demo.backend.dto.ApiResponse;
import com.demo.backend.dto.ArticleCreateDTO;
import com.demo.backend.dto.ArticleResponse;
import com.demo.backend.dto.ArticleUpdateDTO;
import com.demo.backend.dto.PageResponse;
import com.demo.backend.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ApiResponse<ArticleResponse> create(@Valid @RequestBody ArticleCreateDTO dto) {
        return ApiResponse.success(ArticleResponse.from(articleService.create(dto)));
    }

    // 分页查询: GET /api/articles?page=0&size=10
    @GetMapping
    public ApiResponse<PageResponse<ArticleResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ArticleResponse> result = articleService.findAll(
                        PageRequest.of(page, size, Sort.by("createTime").descending()))
                .map(ArticleResponse::from);
        return ApiResponse.success(PageResponse.from(result, result.getContent()));
    }

    // 搜索: GET /api/articles/search?keyword=Spring&page=0&size=10
    @GetMapping("/search")
    public ApiResponse<PageResponse<ArticleResponse>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ArticleResponse> result = articleService.search(keyword,
                        PageRequest.of(page, size, Sort.by("createTime").descending()))
                .map(ArticleResponse::from);
        return ApiResponse.success(PageResponse.from(result, result.getContent()));
    }

    @GetMapping("/{id}")
    public ApiResponse<ArticleResponse> findById(@PathVariable Long id) {
        return ApiResponse.success(ArticleResponse.from(articleService.findById(id)));
    }

    @GetMapping(params = "userId")
    public ApiResponse<List<ArticleResponse>> findByUserId(@RequestParam Long userId) {
        List<ArticleResponse> list = articleService.findByUserId(userId).stream()
                .map(ArticleResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success(list);
    }

    @PutMapping("/{id}")
    public ApiResponse<ArticleResponse> update(@PathVariable Long id, @Valid @RequestBody ArticleUpdateDTO dto) {
        return ApiResponse.success(ArticleResponse.from(articleService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        articleService.delete(id);
        return ApiResponse.success(null);
    }
}