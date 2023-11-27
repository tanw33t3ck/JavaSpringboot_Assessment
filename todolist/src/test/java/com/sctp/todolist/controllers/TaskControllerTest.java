
package com.sctp.todolist.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sctp.todolist.models.Task;
import com.sctp.todolist.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class TaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private Task task1;
    private Task task2;
    private Task updateTask;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        task1 = new Task("task1", true);
        task2 = new Task("task2", false);
        updateTask = new Task("task3", false);
    }

    @Test
    void getAllTasks() throws Exception{
        List<Task> list = new ArrayList<>();
        list.add(task1);
        list.add(task2);

        when(taskService.getAllTask()).thenReturn(list);

        this.mockMvc.perform(get("/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(list.size())));

    }

    @Test
    void getSingleTask() throws Exception{
        when(taskService.findTaskById(1L)).thenReturn(Optional.of(task1));

        this.mockMvc.perform(get("/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.task", is(task1.getTask())));
    }

    @Test
    void getAllCompletedTasks() throws Exception{
        List<Task> list = new ArrayList<>();
        list.add(task1);

        when(taskService.findAllCompletedTask()).thenReturn(list);

        this.mockMvc.perform(get("/completed")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(list.size())));
    }

    @Test
    void getAllIncompleteTasks() throws Exception{
        List<Task> list = new ArrayList<>();
        list.add(task2);

        when(taskService.findAllIncompleteTask()).thenReturn(list);

        this.mockMvc.perform(get("/incomplete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(list.size())));
    }

    @Test
    void createTask() throws Exception{
        when(taskService.createNewTask(any(Task.class))).thenReturn(task1);

        this.mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.task", is(task1.getTask())));
    }

    @Test
    void updateTask() throws Exception{
        Task existingTask = new Task("task1", true);
        existingTask.setId(1L);
        Task newTaskDetails = new Task("task3", false);

        when(taskService.findTaskById(1L)).thenReturn(Optional.of(existingTask));
        when(taskService.updateTask(any(Task.class))).thenReturn(newTaskDetails);

        this.mockMvc.perform(put("/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTaskDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.task", is(newTaskDetails.getTask())))
                .andExpect(jsonPath("$.completed", is(newTaskDetails.isCompleted())));

    }

    @Test
    void deleteTask() throws Exception{

        Task existingTask = new Task("task1", true);
        existingTask.setId(1L);

        when(taskService.findTaskById(1L)).thenReturn(Optional.of(existingTask));
        doNothing().when(taskService).deleteTask(existingTask);

        this.mockMvc.perform(delete("/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}