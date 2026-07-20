# task-manager

## Purpose

提供完整的任务管理功能，包括后端 REST API（CRUD + 状态切换）和前端管理页面（侧边栏导航入口）。

## Requirements

### Requirement: 任务 CRUD 操作
系统须通过 `/api/tasks` 下的 REST 接口提供完整的任务增删改查操作。

#### Scenario: 创建新任务
- **WHEN** 客户端发送 `POST /api/tasks`，携带有效的 `{title, description}`
- **THEN** 系统创建新任务，`done` 为 `false`，自动生成 `id` 和时间戳，以 `ApiResponse<TaskResponse>` 返回

#### Scenario: 缺少标题时创建失败
- **WHEN** 客户端发送 `POST /api/tasks`，`title` 为空或缺失
- **THEN** 系统返回校验错误，状态码 400

#### Scenario: 获取所有任务列表
- **WHEN** 客户端发送 `GET /api/tasks`
- **THEN** 系统按 `createdAt` 降序返回所有任务，包装为 `ApiResponse<List<TaskResponse>>`

#### Scenario: 根据 ID 获取单个任务
- **WHEN** 客户端发送 `GET /api/tasks/{id}`，id 有效存在
- **THEN** 系统返回该任务，包装为 `ApiResponse<TaskResponse>`

#### Scenario: 获取不存在的任务失败
- **WHEN** 客户端发送 `GET /api/tasks/{id}`，id 不存在
- **THEN** 系统返回错误，状态码 404，消息 "task not found"

#### Scenario: 更新任务标题和描述
- **WHEN** 客户端发送 `PUT /api/tasks/{id}`，携带有效的 `{title, description}`
- **THEN** 系统更新任务的标题和描述，刷新 `updatedAt`，返回更新后的任务

#### Scenario: 删除任务
- **WHEN** 客户端发送 `DELETE /api/tasks/{id}`，id 有效存在
- **THEN** 系统删除该任务并返回成功

#### Scenario: 删除不存在的任务失败
- **WHEN** 客户端发送 `DELETE /api/tasks/{id}`，id 不存在
- **THEN** 系统返回错误，状态码 404

### Requirement: 任务状态切换
系统须提供接口用于切换任务的完成状态（已完成 ↔ 未完成）。

#### Scenario: 未完成切换为已完成
- **WHEN** 客户端发送 `PATCH /api/tasks/{id}/toggle`，当前 `done: false`
- **THEN** 系统将 `done` 设为 `true`，更新 `updatedAt`，返回更新后的任务

#### Scenario: 已完成切换为未完成
- **WHEN** 客户端发送 `PATCH /api/tasks/{id}/toggle`，当前 `done: true`
- **THEN** 系统将 `done` 设为 `false`，更新 `updatedAt`，返回更新后的任务

### Requirement: 任务管理前端页面
系统须在路由 `/tasks` 提供任务管理前端页面，通过侧边栏导航访问。

#### Scenario: 认证用户可访问页面
- **WHEN** 已认证用户导航到 `/tasks`
- **THEN** 显示 TaskManager 页面，包含任务列表和创建表单

#### Scenario: 未认证用户被重定向
- **WHEN** 未认证用户导航到 `/tasks`
- **THEN** 重定向到 `/login`

#### Scenario: 侧边栏中包含任务管理入口
- **WHEN** 用户登录后查看侧边栏导航
- **THEN** 侧边栏显示"任务管理"菜单项，点击可进入任务管理页面

#### Scenario: 从界面创建任务
- **WHEN** 用户填写任务创建表单并提交
- **THEN** 新任务立即出现在任务列表中，无需刷新页面

#### Scenario: 从界面编辑任务
- **WHEN** 用户点击任务的编辑按钮，修改字段后保存
- **THEN** 列表中该任务信息更新

#### Scenario: 从界面切换任务状态
- **WHEN** 用户点击任务的状态切换按钮
- **THEN** 任务的完成/未完成状态翻转，界面实时反映变化

#### Scenario: 从界面删除任务
- **WHEN** 用户点击任务的删除按钮并确认
- **THEN** 该任务从列表中移除

### Requirement: 后端遵循现有分层架构
任务模块须遵循与现有模块相同的包结构和模式（Entity → Repository → Service → Controller，使用 DTO 处理请求/响应）。

#### Scenario: 一致的 API 响应格式
- **WHEN** 任何任务接口返回响应
- **THEN** 响应包装在 `ApiResponse<T>` 中，成功时 `code: 200`

#### Scenario: 一致的错误处理
- **WHEN** 任务接口遇到错误
- **THEN** GlobalExceptionHandler 返回包含适当错误码和消息的 `ApiResponse`
