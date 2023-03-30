package com.sys.taskmanager.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/tasks")
public class TaskController {

    private static final Logger log = Logger.getLogger(TaskController.class.getName());
    private final List<Task> taskList = new ArrayList<>();

    @GetMapping
    public ResponseEntity<?> listTasks() {

        log.info("Retrieved " + taskList.size() + " tasks");
        return ResponseEntity.ok().body(taskList);
    }

    @PostMapping
    public ResponseEntity<?> addTask(@RequestBody Task task) {
        try {
            if (taskList.stream().anyMatch(t -> t.getTitle().equals(task.getTitle()))) {
                log.warning("Duplicate task" + task.getTitle());
                throw new DuplicateTaskException();
            }
            taskList.add(task);
            log.info("Added task with title " + task.getTitle());
            return ResponseEntity.status(HttpStatus.OK).body("Task added successfully!");
        } catch (DuplicateTaskException e) {
            log.severe("Error while adding new task" + e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Duplicate Task", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Server error", e.getMessage()));
        }
    }
}
