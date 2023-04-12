package com.sys.taskmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sys.taskmanager.controller.TaskController;
import com.sys.taskmanager.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class TaskControllerTests {

    private Task task;

    private MockMvc taskControllerMock;

    @BeforeEach
    public void setUp() {
        taskControllerMock = MockMvcBuilders.standaloneSetup(new TaskController()).build();
        task = new Task();
        task.setTitle("Task1");
        task.setDescription("Description 1");
        task.setFinished(false);
    }

    @Test
    public void testGetTitle() {
        assertEquals("Task1", task.getTitle());
    }

    @Test
    public void testTitleNotFound() {
        assertNotEquals("Task2", task.getTitle());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Description 1", task.getDescription());
    }

    @Test
    public void testCheckStatus() {
        assertFalse(task.isFinished());
    }

    @Test
    public void testEmptyTaskList() throws Exception {
        taskControllerMock.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    public void testAddTask() throws Exception {
        taskControllerMock.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(task)))
                .andExpect(status().isOk());

        taskControllerMock.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @Test
    public void testAddTaskThrowsClientError() throws Exception {
        taskControllerMock.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(null)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testAddTaskThrowsConflictError() throws Exception {
        taskControllerMock.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(task)))
                .andExpect(status().isOk());

        taskControllerMock.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(task)))
                .andExpect(status().isConflict());
    }

}
