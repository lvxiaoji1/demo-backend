package com.demo.backend.service;

import com.demo.backend.dto.TaskCreateDTO;
import com.demo.backend.dto.TaskUpdateDTO;
import com.demo.backend.entity.Task;
import com.demo.backend.exception.BusinessException;
import com.demo.backend.repository.TaskRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task create(TaskCreateDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDone(false);
        return taskRepository.save(task);
    }

    public List<Task> findAll() {
        return taskRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "task not found: " + id));
    }

    public Task update(Long id, TaskUpdateDTO dto) {
        Task task = findById(id);
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        return taskRepository.save(task);
    }

    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new BusinessException(404, "task not found: " + id);
        }
        taskRepository.deleteById(id);
    }

    public Task toggle(Long id) {
        Task task = findById(id);
        task.setDone(!task.getDone());
        return taskRepository.save(task);
    }
}
