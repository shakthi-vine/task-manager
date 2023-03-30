package com.sys.taskmanager.service;

public class DuplicateTaskException extends Exception{
    public DuplicateTaskException() {
        super("Task with the same title already exists.");
    }
}
