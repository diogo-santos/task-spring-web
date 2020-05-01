package com.todo.web;

import com.todo.web.auth.TodoUserDetailsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("local")
public class TaskControllerTest {
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TodoUserDetailsService userDetailsService;

    private static final String TASK_PATH = "/"+ TaskController.TASKS;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        when(userDetailsService.getUserIdByUsername(anyString())).thenReturn(1L);
    }

    @Test
    public void addTasksPageTest() throws Exception {
        this.mockMvc.perform(get(TASK_PATH))
                    .andExpect(content()
                                .string(containsString("Add task")));
    }

    @Test
    public void givenNullDescriptionWhenAddTasksWhen_ThenReturnFormError() throws Exception {
        this.mockMvc.perform(post(TASK_PATH))
                    .andExpect(model()
                            .attributeHasFieldErrors(TaskController.FORM,"description"))
                    .andExpect(view()
                            .name(TaskController.TASKS));
    }

    @Test
    public void givenEmptyDescriptionWhenAddTasks_ThenReturnFormError() throws Exception {
        this.mockMvc.perform(post(TASK_PATH)
                                .param("description", ""))
                    .andExpect(model()
                                .attributeHasFieldErrors(TaskController.FORM,"description"))
                    .andExpect(view()
                                .name(TaskController.TASKS));
    }

    @Test
    public void givenValidDescriptionWhenAddTasks_ThenReturnAddedTask() throws Exception {
        this.mockMvc.perform(post(TASK_PATH)
                .param("description", "Task description"))
                .andExpect(model()
                        .hasNoErrors())
                .andExpect(content()
                        .string(containsString("Task description")))
                .andExpect(view()
                        .name(TaskController.TASKS));
    }
}