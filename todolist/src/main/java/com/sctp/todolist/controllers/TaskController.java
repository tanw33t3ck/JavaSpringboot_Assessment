package com.sctp.todolist.controllers;


import com.sctp.todolist.exception.ResourceNotFoundException;
import com.sctp.todolist.models.Task;
import com.sctp.todolist.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller

public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping("/")
    public ResponseEntity<?> getAllTasks(){
        List<Task> tasks = taskService.getAllTask();
        if(!tasks.isEmpty()){
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No tasks available", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getSingleTask(@PathVariable("id") Long id){
        Task result = taskService.findTaskById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/completed")
    public ResponseEntity<List<Task>> getAllCompletedTasks(){
        List<Task> completedTasks = taskService.findAllCompletedTask();

        if (completedTasks != null && !completedTasks.isEmpty()) {
            return ResponseEntity.ok(completedTasks);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @GetMapping("/incomplete")
    public ResponseEntity<List<Task>> getAllIncompleteTasks(){
        List<Task> incompleteTasks = taskService.findAllIncompleteTask();

        if (incompleteTasks != null && !incompleteTasks.isEmpty()) {
            return ResponseEntity.ok(incompleteTasks);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @PostMapping("/")
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task){
        try{
            Task result = taskService.createNewTask(task);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Could not create the task", e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable("id") Long id, @Valid @RequestBody Task taskDetails) {
        try{
            Task task = taskService.findTaskById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(id));

            task.setTask(taskDetails.getTask());
            task.setCompleted(taskDetails.isCompleted());

            Task updatedTask = taskService.updateTask(task);
            return ResponseEntity.ok(updatedTask);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Task not found with id " + id, e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Could not update the task", e);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable("id") Long id) {
        Optional<Task> taskOptional = taskService.findTaskById(id);
        if (taskOptional.isPresent()) {
            taskService.deleteTask(taskOptional.get());
            return new ResponseEntity<>("Task deleted successfully", HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException(id);
        }
    }
}
