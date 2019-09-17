package ru.job4j.todolist;

import java.util.List;

public interface ToDoStore {
    Task create(Task task);
    int update(int id, Task task);
    int delete(int id);
    Task findTaskById(int id);
    List<Task> findAll();
    List<Task> findTaskByName(String name);
}
