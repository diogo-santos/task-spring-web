package com.todo.web;

import com.todo.web.domain.TaskDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TaskService {

    private List<TaskDto> taskDtos;

    public TaskService() {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);
        taskDto.setDescription("Task description");
        taskDto.setChecked(false);
        taskDto.setLastUpdate(LocalDateTime.now());
        taskDtos = new ArrayList<>(Collections.singletonList(taskDto));
    }

    public void save(Long userId, TaskDto task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(this.taskDtos.stream()
                .mapToLong(TaskDto::getId)
                .max()
                .orElse(0L) + 1);
        taskDto.setDescription(task.getDescription());
        taskDto.setChecked(false);
        taskDto.setLastUpdate(LocalDateTime.now());
        this.taskDtos.add(taskDto);
    }

    public void updateStatus(Long userId, Long taskId) {
        this.taskDtos.stream()
                .filter(taskDto -> taskDto.getId() == taskId)
                .forEach(taskDto -> taskDto.setChecked(!taskDto.getChecked()));
    }

    public void remove(Long userId, Long taskId) {
        this.taskDtos.removeIf(taskDto -> taskDto.getId() == taskId);
    }

    public List<TaskDto> find(Long userId) {
        return this.taskDtos;
    }
}
