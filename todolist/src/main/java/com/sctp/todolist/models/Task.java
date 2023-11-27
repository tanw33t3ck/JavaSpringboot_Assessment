package com.sctp.todolist.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // this is the primary key which will be auto generated
    private Long id;

    @NotBlank(message = "Task description cannot be blank")
    private String task;

    @NotNull(message = "Task completion cannot be blank")
    private boolean completed;

    public Task() {
    }

    public Task(String task, boolean completed){
        this.task = task;
        this.completed = completed;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getTask(){
        return task;
    }

    public void setTask(String task){
        this.task = task;
    }

    public boolean isCompleted(){
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
