package com.sys.taskmanager.repository;

import com.sys.taskmanager.entity.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<TaskEntity, String> {
    Page<TaskEntity> findAll(Pageable pageable);
}
