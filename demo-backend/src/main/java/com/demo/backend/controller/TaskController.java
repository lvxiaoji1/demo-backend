package com.demo.backend.controller;

import com.demo.backend.dto.ApiResponse;
import com.demo.backend.dto.TaskCreateDTO;
import com.demo.backend.dto.TaskResponse;
import com.demo.backend.dto.TaskUpdateDTO;
import com.demo.backend.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ApiResponse<TaskResponse> create(@Valid @RequestBody TaskCreateDTO dto) {
        return ApiResponse.success(TaskResponse.from(taskService.create(dto)));
    }

    @GetMapping
    public ApiResponse<List<TaskResponse>> findAll() {
        List<TaskResponse> list = taskService.findAll().stream()
                .map(TaskResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success(list);
    }

    @GetMapping("/{id}")
    public ApiResponse<TaskResponse> findById(@PathVariable Long id) {
        return ApiResponse.success(TaskResponse.from(taskService.findById(id)));
    }

    @PutMapping("/{id}")
    public ApiResponse<TaskResponse> update(@PathVariable Long id, @Valid @RequestBody TaskUpdateDTO dto) {
        return ApiResponse.success(TaskResponse.from(taskService.update(id, dto)));
    }

    @PatchMapping("/{id}/toggle")
    public ApiResponse<TaskResponse> toggle(@PathVariable Long id) {
        return ApiResponse.success(TaskResponse.from(taskService.toggle(id)));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ApiResponse.success(null);
    }
}
