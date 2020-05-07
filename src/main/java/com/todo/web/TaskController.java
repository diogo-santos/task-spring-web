package com.todo.web;

import com.todo.web.auth.TodoUserDetailsService;
import com.todo.web.domain.TaskDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class TaskController {

    private final TaskService taskService;
    private final TodoUserDetailsService userDetailsService;

    public static final String TASKS = "tasks";
    public static final String FORM  = "taskForm";

    public TaskController(TaskService taskService, TodoUserDetailsService userDetailsService) {
        this.taskService = taskService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping(TASKS)
    public String getTaskList(Model model, Authentication authentication) {
        Long userId = this.userDetailsService.getCurrentUserId(authentication.getName());
        return this.getTaskList(model, userId);
    }

    @PostMapping(TASKS)
    public String addTask(Model model, Authentication authentication, @Valid TaskForm form, BindingResult result) {
        Long userId = this.userDetailsService.getCurrentUserId(authentication.getName());
        if (!result.hasErrors()) {
            TaskDto taskDto = new TaskDto();
            taskDto.setDescription(form.getDescription());
            taskService.save(userId, taskDto);

            return "redirect:/" + this.getTaskList(model, userId);
        }

        return TASKS;
    }

    @RequestMapping("/tasks/{id}")
    public String removeTask(Model model, Authentication authentication, @PathVariable final Long id) {
        Long userId = this.userDetailsService.getCurrentUserId(authentication.getName());
        taskService.remove(userId, id);

        return "redirect:/" + this.getTaskList(model, userId);
    }

    @RequestMapping("/tasks/{id}/status")
    public String updateTaskStatus(Model model, Authentication authentication, @PathVariable final Long id) {
        Long userId = this.userDetailsService.getCurrentUserId(authentication.getName());
        taskService.updateStatus(userId, id);

        return "redirect:/" + this.getTaskList(model, userId);
    }

    private String getTaskList(Model model, final Long userId) {
        model.addAttribute(FORM, new TaskForm());
        List<TaskDto> taskList = this.taskService.find(userId);
        model.addAttribute(TASKS, taskList);
        return TASKS;
    }
}