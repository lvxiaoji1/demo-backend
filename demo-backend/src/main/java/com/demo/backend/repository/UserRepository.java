package com.demo.backend.repository;

import com.demo.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户 Repository —— 数据访问层。
 *
 * JpaRepository<User, Long> 提供了内置的 CRUD 方法：
 *   findAll()、findById()、save()、deleteById() 等
 * 不需要手写实现类，Spring Data JPA 会自动生成。
 *
 * 如果想加自定义查询，比如根据邮箱查找，只需声明方法：
 *   User findByEmail(String email);
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
