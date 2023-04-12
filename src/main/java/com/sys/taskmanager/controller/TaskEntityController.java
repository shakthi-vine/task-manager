package com.sys.taskmanager.controller;

import com.sys.taskmanager.entity.TaskEntity;
import com.sys.taskmanager.helper.ResponseBuilder;
import com.sys.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/tasks")
public class TaskEntityController {
    @Autowired
    protected TaskRepository taskRepository;

    @GetMapping
    public ResponseEntity<?> getTaskList(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "2") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskEntity> page1 = taskRepository.findAll(pageable);
//        List<?> taskList = StreamSupport.stream(taskRepository.findAll(pageable).spliterator(), false).toList();
        return ResponseBuilder.fromPageSuccess(page1);
    }

    @PostMapping
    public ResponseEntity<?> addTask(@RequestBody TaskEntity task) {
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            return ResponseBuilder.fromInvalidRequest("Title attribute not found");
        }
        Optional<TaskEntity> task1 = taskRepository.findById(task.getTitle());
        if (task1.isPresent()) {
            return ResponseBuilder.fromResourceNotFound("Task with the title is already exists, " + task.getTitle());
        }
        taskRepository.save(task);
        return ResponseBuilder.fromSuccess("Task Added Successfully");
    }
}
