package com.todo.web;

import com.todo.web.domain.TaskDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.todo.web.TaskController.FORM;
import static com.todo.web.TaskController.TASKS;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithUserDetails(userDetailsServiceBeanName = "todoUserDetailsService", value = "userTest1")
public class TaskControllerTest {
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaskService taskService;

    private static final String TASK_PATH = "/"+ TASKS;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();

        when(taskService.find(anyLong()))
                .thenReturn(new TaskDto[]{new TaskDto("Task description")});
    }

    @Test
    public void getTasksPageTest() throws Exception {
        this.mockMvc.perform(get(TASK_PATH))
                .andExpect(content().string(containsString("Add task")))
                .andExpect(content().string(containsString("Task description")));
    }

    @Test
    public void givenNullDescriptionWhenAddTasksWhen_ThenReturnFormError() throws Exception {
        this.mockMvc.perform(post(TASK_PATH))
                .andExpect(model().attributeHasFieldErrors(FORM,"description"))
                .andExpect(view().name(TASKS));
    }

    @Test
    public void givenEmptyDescriptionWhenAddTasks_ThenReturnFormError() throws Exception {
        this.mockMvc.perform(post(TASK_PATH).param("description", ""))
                .andExpect(model().attributeHasFieldErrors(FORM,"description"))
                .andExpect(view().name(TASKS));
    }

    @Test
    public void givenValidDescriptionWhenAddTasks_ThenReturnAddedTask() throws Exception {
        this.mockMvc.perform(post(TASK_PATH)
                .param("description", "Task description"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TASK_PATH));
    }

    @Test
    public void givenTaskIdWhenRemoveTask_ThenTaskIsRemoved() throws Exception {
        this.mockMvc.perform(delete("/tasks/{id}","1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TASK_PATH));
    }

    @Test
    public void givenTaskIdWhenUpdateTaskStatus_ThenTaskStatusIsUpdated() throws Exception {
        this.mockMvc.perform(put("/tasks/{id}/status","1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TASK_PATH));
    }
}