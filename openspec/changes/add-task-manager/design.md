## 背景

这是一个 Spring Boot 3.4 + Vue 3.5 全栈项目，使用 JWT 认证。现有架构遵循一致的分层模式：后端 Entity → Repository → Service → Controller，前端 API 客户端 → 页面组件 → 路由。所有 API 响应使用统一的 `ApiResponse<T>` 包装，前端 axios 拦截器自动解包。

项目现有用户、文章、分类三个模块。此次新增任务管理模块的同时，将前端整体布局从顶部 tab 重构为侧边栏导航。

## 目标 / 非目标

**目标：**
- 完整的任务 CRUD 管理（创建、列表、更新、删除）
- 任务完成状态切换（已完成 ↔ 未完成）
- 前端侧边栏布局，所有模块统一导航入口
- 遵循现有后端分层模式（Entity → Repo → Service → Controller → DTO）
- 遵循现有前端模式（API 模块 → SFC 组件 → 路由条目）

**非目标：**
- 不做任务与用户的关联（任务为全局资源，后续可扩展）
- 不设截止日期、优先级或任务分类（最小可用模块）
- 不做 WebSocket 实时更新
- 不做拖拽排序

## 技术决策

### 1. 任务数据模型

**决策**：最小化实体，字段包括 `id`、`title`、`description`、`done`（布尔值）、`createdAt`、`updatedAt`。

**理由**：简单任务管理器只需这些字段。`done` 布尔值支持状态切换。时间戳由 JPA 自动管理。

**备选方案**：
- 状态枚举（TODO/IN_PROGRESS/DONE）—— 对 v1 过于复杂，布尔值加切换更简洁够用。
- `userId` 外键关联用户 —— v1 不做，任务为全局资源，与 Category 模式一致。

### 2. 任务不绑定用户

**决策**：任务为全局资源，任何认证用户均可查看和管理所有任务，与 Category 模式一致。

**理由**：保持与现有 CategoryManager 模式一致。用户级任务隔离可后续扩展。

### 3. 前端布局：侧边栏导航

**决策**：将 `App.vue` 从顶部 tab 栏重构为 220px 宽深色侧边栏，包含所有模块入口（仪表盘、任务管理、用户管理、文章管理、分类管理），当前路由高亮显示。

**理由**：用户明确不想用顶部 tab 模式。侧边栏是替代 tab 模式的最佳方案，导航更清晰、可扩展性更好。

**备选方案**：
- 仪表盘添加入口卡片 —— 任务管理入口不够直观。
- 独立布局页面 —— 过度复杂，侧边栏统一管理所有模块更合理。

### 4. 前端 API 客户端

**决策**：在现有 `demo-frontend/src/api/index.js` 中新增 `taskApi`，与 `categoryApi`、`articleApi` 模式完全一致。

**理由**：保持一致性。所有 API 调用通过集中式 axios 实例和拦截器。

### 5. 后端结构

**决策**：完全遵循 Category 模块的模式：
- `entity/Task.java`
- `repository/TaskRepository.java`
- `service/TaskService.java`
- `controller/TaskController.java`
- `dto/TaskCreateDTO.java`、`dto/TaskUpdateDTO.java`、`dto/TaskResponse.java`

**理由**：Category 模块是现有最简单的模块，无关联关系，纯 CRUD，最适合作为模板。

### 6. 更新接口设计

**决策**：`PUT /api/tasks/{id}` 接收 `TaskUpdateDTO`（标题和描述）。单独的 `PATCH /api/tasks/{id}/toggle` 接口切换 `done` 状态。

**理由**：将状态切换与完整更新分离，API 更清晰。切换是独立操作，不需要发送完整任务体。

## 风险 / 取舍

- **任务无用户隔离**：任何认证用户可修改任何任务。→ 缓解：与 Category 模块模式一致。若后续需要用户级隔离，添加 `userId` 字段并做数据迁移。
- **无分页**：任务列表可能增长很大。→ 缓解：对于演示/待办应用，分页是过度设计。后续需要时再加。
