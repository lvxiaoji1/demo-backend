package com.demo.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 第一个 REST 控制器，演示 HTTP GET 请求处理。
 *
 * {@code @RestController} = {@code @Controller} + {@code @ResponseBody}
 * 表示每个方法的返回值直接写入 HTTP 响应 body（JSON），不走视图解析。
 */
@RestController
@RequestMapping("/api")  // 所有该控制器中的路径都以 /api 开头
public class HelloController {

    /**
     * GET /api/hello
     * 返回一个简单的问候信息。
     *
     * {@code @GetMapping} 是 {@code @RequestMapping(method = RequestMethod.GET)} 的简写。
     */
    @GetMapping("/hello")
    public Map<String, Object> hello() {
        // Spring Boot 自动将 Map 序列化为 JSON（通过 Jackson 库）
        return Map.of(
                "message", "Hello, 后端世界!",
                "time", LocalDateTime.now().toString()
        );
    }
}
