package com.todo.web;

import com.todo.web.domain.TaskDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class TaskService {

    public TaskService() {
    }

    public void save(TaskDto task) {

    }

    public void updateStatus(Long taskId) {

    }

    public void remove(Long taskId) {

    }

    public List<TaskDto> find() {
        TaskDto taskDto = new TaskDto();
        taskDto.setDescription("Task description");
        taskDto.setDone(false);
        taskDto.setLastUpdate(LocalDateTime.now());
        return Collections.singletonList(taskDto);
    }
}
