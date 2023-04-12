package com.sys.taskmanager.helper;

import com.sys.taskmanager.entity.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseBuilder {

    public static class ApiResponse {
        public static class Builder {
            private boolean success;
            private String message;
            private Object data;

            private Page<TaskEntity> page;

            public Builder success(boolean success) {
                this.success = success;
                return this;
            }

            public Builder message(String message) {
                this.message = message;
                return this;
            }

            public Builder data(Object data) {
                this.data = data;
                return this;
            }

            public Builder page(Page<TaskEntity> page) {
                this.page = page;
                return this;
            }

            public Map<String, Object> build() {
                Map<String, Object> response = new HashMap<>();
                response.put("success", success);
                if (message != null)
                    response.put("message", message);
                if (data != null)
                    response.put("data", data);
                if (page != null) {
                    response.put("data", page.getContent());
                    response.put("total_pages", page.getTotalPages());
                    response.put("total_items", page.getTotalElements());
                    response.put("size", page.getSize());
                }
                return response;
            }
        }
    }

    public static ResponseEntity<?> fromSuccess(List<?> items) {
        Map<String, Object> response = new ApiResponse.Builder().success(true).data(items).build();
        return ResponseEntity.ok().body(response);
    }

    public static ResponseEntity<?> fromPageSuccess(Page<TaskEntity> page) {
        Map<String, Object> response = new ApiResponse.Builder().success(true).page(page).build();
        return ResponseEntity.ok().body(response);
    }

    public static ResponseEntity<?> fromSuccess(String message) {
        Map<String, Object> response = new ApiResponse.Builder().success(true).message(message).build();
        return ResponseEntity.ok().body(response);
    }

    public static ResponseEntity<?> fromInvalidRequest(String message) {
        Map<String, Object> response = new ApiResponse.Builder().success(false).message(message).build();
        return ResponseEntity.badRequest().body(response);
    }

    public static ResponseEntity<?> fromResourceNotFound(String message) {
        Map<String, Object> response = new ApiResponse.Builder().success(false).message(message).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
