package com.todo.web;

import com.todo.web.domain.TaskDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class TaskController {

    private final TaskService taskService;

    public static final String TASKS = "tasks";
    public static final String FORM  = "taskForm";

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(TASKS)
    public String taskList(Model model) {
        setupModel(model);

        return TASKS;
    }

    @PostMapping(TASKS)
    public String addTask(Model model, @Valid TaskForm form, BindingResult result) {
        if (!result.hasErrors()) {
            TaskDto taskDto = new TaskDto();
            taskDto.setDescription(form.getDescription());
            taskService.save(taskDto);
            model.addAttribute(FORM, new TaskForm());
        }
        List<TaskDto> taskList = taskService.find();
        model.addAttribute(TASKS, taskList);

        return TASKS;
    }

    @RequestMapping("tasks/{taskId}")
    public String removeTask(Model model, @PathVariable final Long taskId) {
        taskService.remove(taskId);
        setupModel(model);

        return TASKS;
    }

    @RequestMapping("task̍s/{taskId}/status")
    public String updateStatus(Model model, @PathVariable final Long taskId) {
        taskService.updateStatus(taskId);
        setupModel(model);

        return TASKS;
    }

    private void setupModel(Model model) {
        model.addAttribute(FORM, new TaskForm());
        List<TaskDto> taskList = taskService.find();
        model.addAttribute(TASKS, taskList);
    }
}