package com.sys.taskmanager.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task {
    private String title;
    private String description;
    private boolean finished;
}
