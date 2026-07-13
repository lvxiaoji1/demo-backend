package com.demo.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 应用程序入口类。
 *
 * {@code @SpringBootApplication} 是一个组合注解，等价于：
 *   - {@code @Configuration} —— 标记为配置类
 *   - {@code @EnableAutoConfiguration} —— 启用 Spring Boot 的自动配置
 *   - {@code @ComponentScan} —— 扫描当前包及其子包的组件
 */
@SpringBootApplication
public class DemoBackendApplication {

    public static void main(String[] args) {
        // SpringApplication.run() 启动内嵌的 Tomcat 服务器
        SpringApplication.run(DemoBackendApplication.class, args);
    }
}
