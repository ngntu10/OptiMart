package com.github.nhatoriginal.spring.doc.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

  @Autowired
  private TaskRepository taskRepository;

  public List<Task> getTaskList() {
    return taskRepository.findAll();
  }

  public Task getTaskById(Long id) {
    return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
  }

  public Task createTask(Task task) {
    return taskRepository.save(task);
  }

  public Task deleteTaskById(Long id) {
    Task task = getTaskById(id);
    taskRepository.delete(task);
    return task;
  }

  public Task updateTaskById(Long id, Task task) {
    Task taskToUpdate = getTaskById(id);
    taskToUpdate.setTitle(task.getTitle());
    taskToUpdate.setDescription(task.getDescription());
    taskToUpdate.setCompleted(task.isCompleted());
    return taskRepository.save(taskToUpdate);
  }
}
