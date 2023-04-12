package com.sys.taskmanager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TaskEntity {
    @Id
    private String title;
    private String description;
    private boolean finished;
}
