package com.sys.taskmanager.controller;

import com.sys.taskmanager.exceptions.DuplicateException;
import com.sys.taskmanager.exceptions.NotFoundException;
import com.sys.taskmanager.model.Task;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static com.sys.taskmanager.helper.ResponseBuilder.ApiResponse;

@RestController
@RequestMapping("v1/api/tasks")
public class TaskController {

    private static final Logger log = Logger.getLogger(TaskController.class.getName());
    private final List<Task> taskList = new ArrayList<>();

    @GetMapping
    public ResponseEntity<?> listTasks(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "2") int size,
                                       @RequestParam(required = false) String title) {

        log.info("Retrieving task list..");
        int totalTasks = taskList.size();
        int pageNo = page * size;
        int pageSize = Math.min(pageNo + size, totalTasks);

        if(pageNo >= totalTasks) {
            return ResponseEntity.ok().body(new ApiResponse.Builder().message("Task List").success(true)
                    .data(new ArrayList<>()).build());
        }

        List<Task> updatedTaskList;
        if (title != null && !title.isEmpty()) {
            updatedTaskList = taskList.stream().filter(t -> t.getTitle().contains(title)).toList().subList(pageNo, pageSize);
        } else {
            updatedTaskList = taskList.subList(pageNo, pageSize);
        }
        Map<String, Object> response = new ApiResponse.Builder().success(true).message("Task List")
                .data(updatedTaskList).build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<?> addTask(@Valid @RequestBody Task task, BindingResult bindingResult) {
        try {
            if(bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(new ApiResponse.Builder().success(false)
                        .message("Add Task Failed").data(bindingResult.getAllErrors()).build());
            }
            if (taskList.stream().anyMatch(t -> t.getTitle().equals(task.getTitle()))) {
                log.warning("Duplicate task, " + task.getTitle());
                throw new DuplicateException("Task with same title already exist");
            }
            taskList.add(task);
            Map<String, Object> response = new ApiResponse.Builder().success(true)
                    .message("Task added successfully").data(task).build();
            return ResponseEntity.ok().body(response);
        } catch (DuplicateException e) {
            log.severe(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse.Builder().success(false).message(e.getMessage()).data(task).build());
        } catch (Exception e) {
            log.severe(e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse.Builder().success(false).message(e.getMessage()).build());
        }
    }

    @PutMapping("{title}")
    public ResponseEntity<?> updateTask(@RequestBody Task task, @PathVariable String title) {
        try {
            if (title != null && !title.isEmpty()) {
                Task taskToBeUpdated = taskList.stream().filter(t -> t.getTitle().equals(title))
                        .findFirst().orElseThrow(() -> new NotFoundException("Task with the title is not found"));

                taskToBeUpdated.setTitle(task.getTitle());
                taskToBeUpdated.setDescription(task.getDescription());
                taskToBeUpdated.setFinished(task.isFinished());
                return ResponseEntity.ok().body(new ApiResponse.Builder().success(true)
                        .message("Task updated successfully").data(task).build());
            } else {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse.Builder().success(false).message("Bad Request").build());
            }
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse.Builder().success(false).message(e.getMessage()).data(task).build());
        }
    }
}
