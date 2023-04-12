package com.sys.taskmanager.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Task {
    @NotNull(message = "Title cannot be null")
    private String title;
    private String description;
    private boolean finished;
}
