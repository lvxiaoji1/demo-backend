## 1. 后端数据层

- [x] 1.1 创建 `Task` 实体（`entity/Task.java`），字段：id（自增）、title（@NotBlank）、description（@Lob TEXT）、done（布尔，默认 false）、createdAt、updatedAt。使用 Lombok，`@PrePersist` 和 `@PreUpdate` 管理时间戳。
- [x] 1.2 创建 `TaskRepository` 接口（`repository/TaskRepository.java`），继承 `JpaRepository<Task, Long>`。

## 2. 后端 DTO

- [x] 2.1 创建 `TaskCreateDTO`（`dto/TaskCreateDTO.java`），含 `@NotBlank String title` 和可选 `String description`。
- [x] 2.2 创建 `TaskUpdateDTO`（`dto/TaskUpdateDTO.java`），含 `@NotBlank String title` 和可选 `String description`。
- [x] 2.3 创建 `TaskResponse`（`dto/TaskResponse.java`），含 id、title、description、done、createdAt、updatedAt，以及静态工厂方法 `from(Task)`。

## 3. 后端 Service

- [x] 3.1 创建 `TaskService`（`service/TaskService.java`），方法：`create(TaskCreateDTO)`、`findAll()`（按 createdAt 降序）、`findById(Long)`、`update(Long, TaskUpdateDTO)`、`delete(Long)`、`toggle(Long)`。不存在时抛出 `BusinessException(404, ...)`。

## 4. 后端 Controller

- [x] 4.1 创建 `TaskController`（`controller/TaskController.java`），接口如下，均返回 `ApiResponse<T>`：
  - `POST /api/tasks` — 创建任务
  - `GET /api/tasks` — 获取全部任务列表
  - `GET /api/tasks/{id}` — 获取单个任务
  - `PUT /api/tasks/{id}` — 更新标题和描述
  - `PATCH /api/tasks/{id}/toggle` — 切换完成状态
  - `DELETE /api/tasks/{id}` — 删除任务

## 5. 前端 API 客户端

- [x] 5.1 在 `demo-frontend/src/api/index.js` 中新增 `taskApi`，方法：`findAll()`、`findById(id)`、`create(data)`、`update(id, data)`、`delete(id)`、`toggle(id)`。

## 6. 前端页面组件

- [x] 6.1 在 `demo-frontend/src/components/` 中创建 `TaskManager.vue`，包含：
  - 任务创建/编辑表单（标题输入框、可选描述文本域、提交按钮）
  - 任务列表表格：标题、描述、完成状态标签、操作按钮
  - 编辑模式：点击编辑后表单切换为编辑模式
  - 状态切换按钮：点击翻转完成/未完成，已完成行标题加删除线
  - 删除按钮：移除任务
  - 使用 `api/index.js` 中统一的 `taskApi`（不单独创建 axios 实例）
  - 加载中和空数据状态
  - 错误提示

## 7. 前端布局与路由

- [x] 7.1 重构 `App.vue`：顶部 tab 导航改为 220px 深色侧边栏布局，含所有模块入口（仪表盘、任务管理、用户管理、文章管理、分类管理），当前路由高亮，底部显示用户名和登出按钮。
- [x] 7.2 在 `demo-frontend/src/router/index.js` 中新增 `/tasks` 路由：`{ path: '/tasks', name: 'Tasks', component: TaskManager, meta: { requiresAuth: true } }`。

## 8. 验证

- [x] 8.1 验证后端编译无报错，确认所有 6 个 API 接口可访问。
- [x] 8.2 验证前端构建成功，`/tasks` 页面正常加载（认证用户可访问，未认证用户跳转登录页）。
- [x] 8.3 验证所有 CRUD 操作端到端可用（创建、列表、编辑、切换状态、删除）。
- [x] 8.4 验证侧边栏中任务管理入口可见且可正常导航。
