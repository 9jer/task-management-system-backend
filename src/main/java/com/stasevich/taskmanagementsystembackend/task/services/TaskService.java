package com.stasevich.taskmanagementsystembackend.task.services;

import com.stasevich.taskmanagementsystembackend.task.models.Task;
import com.stasevich.taskmanagementsystembackend.task.models.TaskPriority;
import com.stasevich.taskmanagementsystembackend.task.models.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    List<Task> findAllTask();
    Task findTaskById(Long id);
    Task createNewTask(Task task);
    Task updateTaskStatus(Long id, TaskStatus status);
    Task updateTaskPriority(Long id, TaskPriority priority);
    Task updateTaskAssignees(Long id, List<Long> assigneesIds);
    Task updateTask(Long id, Task updatedTask);
    Page<Task> findTasksByAuthor(Long id, Pageable pageable);
    Page<Task> findTaskByAssignee(Long id, Pageable pageable);
    void deleteTaskById(Long id);
    Page<Task> findTasksByFilters(Long authorId, Long assigneeId, TaskStatus status, TaskPriority priority, Pageable pageable);

}
