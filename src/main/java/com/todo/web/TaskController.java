package com.todo.web;

import com.todo.web.auth.TodoUserDetailsService;
import com.todo.web.domain.TaskDto;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String taskList(Model model) {
        setupModel(model);

        return TASKS;
    }

    @PostMapping(TASKS)
    public String addTask(Model model, @Valid TaskForm form, BindingResult result) {
        Long userId = this.getUserId();
        if (!result.hasErrors()) {
            TaskDto taskDto = new TaskDto();
            taskDto.setDescription(form.getDescription());
            taskService.save(userId, taskDto);
            model.addAttribute(FORM, new TaskForm());
        }
        List<TaskDto> taskList = taskService.find(userId);
        model.addAttribute(TASKS, taskList);

        return TASKS;
    }

    @RequestMapping("tasks/{id}")
    public String removeTask(Model model, @PathVariable final Long id) {
        Long userId = this.getUserId();
        taskService.remove(userId, id);
        setupModel(model);

        return TASKS;
    }

    @RequestMapping("task̍s/status/{id}")
    public String updateTaskStatus(Model model, @PathVariable final Long id) {
        Long userId = this.getUserId();
        taskService.updateStatus(userId, id);
        setupModel(model);

        return TASKS;
    }

    private void setupModel(Model model) {
        model.addAttribute(FORM, new TaskForm());
        Long userId = this.getUserId();
        List<TaskDto> taskList = taskService.find(userId);
        model.addAttribute(TASKS, taskList);
    }

    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null != authentication
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return userDetailsService.getUserIdByUsername(authentication.getName());
        }
        return null;
    }
}