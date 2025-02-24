package com.stasevich.taskmanagementsystembackend.task.controllers;

import com.stasevich.taskmanagementsystembackend.task.dto.AssigneesUpdateRequest;
import com.stasevich.taskmanagementsystembackend.task.dto.TaskDto;
import com.stasevich.taskmanagementsystembackend.task.dto.SaveTaskDto;
import com.stasevich.taskmanagementsystembackend.task.dto.TasksResponse;
import com.stasevich.taskmanagementsystembackend.task.models.Task;
import com.stasevich.taskmanagementsystembackend.task.models.TaskPriority;
import com.stasevich.taskmanagementsystembackend.task.models.TaskStatus;
import com.stasevich.taskmanagementsystembackend.task.services.TaskService;
import com.stasevich.taskmanagementsystembackend.task.services.TaskServiceImpl;
import com.stasevich.taskmanagementsystembackend.task.util.ErrorsUtil;
import com.stasevich.taskmanagementsystembackend.task.util.TaskException;
import com.stasevich.taskmanagementsystembackend.util.ErrorResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final ModelMapper modelMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskDto> createTask(@RequestBody @Valid SaveTaskDto taskDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ErrorsUtil.returnAllErrors(bindingResult);
        }

        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON)
                .body(convertTaskToTaskDto(taskService.createNewTask(convertSaveTaskDtoToTask(taskDto))));
/*        return new ResponseEntity<>(convertTaskToTaskDto(taskService
                .createNewTask(convertSaveTaskDtoToTask(taskDto))), HttpStatus.CREATED);*/
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskDto> updateTask( @PathVariable(name = "id") Long id,
                                               @RequestBody @Valid SaveTaskDto taskDto, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            ErrorsUtil.returnAllErrors(bindingResult);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(convertTaskToTaskDto(taskService.updateTask(id, convertSaveTaskDtoToTask(taskDto))));
/*        return new ResponseEntity<>(convertTaskToTaskDto(taskService.updateTask(id, convertSaveTaskDtoToTask(taskDto))), HttpStatus.OK);*/
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TasksResponse> findAllTasks() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new TasksResponse(taskService.findAllTask().stream()
                        .map(this::convertTaskToTaskDto).toList()));
/*        return new ResponseEntity<>(new TasksResponse(taskService.findAllTask().stream()
                .map(this::convertTaskToTaskDto).toList()), HttpStatus.OK);*/
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskDto> findTaskById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(convertTaskToTaskDto(taskService.findTaskById(id)));
        //return new ResponseEntity<>(convertTaskToTaskDto(taskService.findTaskById(id)), HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<TaskDto> updateTaskStatus(@PathVariable(name = "id") Long id, @RequestParam(name = "status") TaskStatus taskStatus) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(convertTaskToTaskDto(taskService.updateTaskStatus(id, taskStatus)));
        //return new ResponseEntity<>(convertTaskToTaskDto(taskService.updateTaskStatus(id, taskStatus)), HttpStatus.OK);
    }

    @PatchMapping("/{id}/priority")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskDto> updateTaskPriority(@PathVariable(name = "id") Long id, @RequestParam(name = "priority") TaskPriority taskPriority){
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(convertTaskToTaskDto(taskService.updateTaskPriority(id, taskPriority)));
        //return new ResponseEntity<>(convertTaskToTaskDto(taskService.updateTaskPriority(id, taskPriority)), HttpStatus.OK);
    }

    @PatchMapping("/{id}/assignees")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskDto> updateTaskAssignees(@PathVariable(name = "id") Long id, @RequestBody AssigneesUpdateRequest assigneesIds) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(convertTaskToTaskDto(taskService.updateTaskAssignees(id, assigneesIds.getAssigneesIds())));
        //return new ResponseEntity<>(convertTaskToTaskDto(taskService.updateTaskAssignees(id, assigneesIds.getAssigneesIds())), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTaskById(@PathVariable(name = "id") Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.ok().build();
        //return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/author/{authorId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<TasksResponse> getTasksByAuthor(
            @PathVariable(name = "authorId") Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> taskPage = taskService.findTasksByAuthor(authorId, pageable);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new TasksResponse(taskPage.getContent().stream()
                        .map(this::convertTaskToTaskDto).toList()));
/*        return new ResponseEntity<>(new TasksResponse(taskPage.getContent().stream()
                .map(this::convertTaskToTaskDto).toList()), HttpStatus.OK);*/
    }

    @GetMapping("/assignee/{assigneeId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<TasksResponse> getTasksByAssignee(
            @PathVariable(name = "assigneeId") Long assigneeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> taskPage = taskService.findTaskByAssignee(assigneeId, pageable);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new TasksResponse(taskPage.getContent().stream()
                        .map(this::convertTaskToTaskDto).toList()));
   /*     return new ResponseEntity<>(new TasksResponse(taskPage.getContent().stream()
                .map(this::convertTaskToTaskDto).toList()), HttpStatus.OK);*/
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<TasksResponse> searchTasksByFilters(
            @RequestParam(name = "authorId", required = false) Long authorId,
            @RequestParam(name = "assigneeId", required = false) Long assigneeId,
            @RequestParam(name = "status", required = false) TaskStatus status,
            @RequestParam(name = "priority", required = false) TaskPriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> taskPage = taskService.findTasksByFilters(authorId, assigneeId, status, priority, pageable);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new TasksResponse(taskPage.getContent().stream()
                        .map(this::convertTaskToTaskDto).toList()));
      /*  return new ResponseEntity<>(new TasksResponse(taskPage.getContent().stream()
                .map(this::convertTaskToTaskDto).toList()), HttpStatus.OK);*/
    }

    private TaskDto convertTaskToTaskDto(Task task) {
        return modelMapper.map(task, TaskDto.class);
    }

    private Task convertSaveTaskDtoToTask(SaveTaskDto taskDto) {
        return modelMapper.map(taskDto, Task.class);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(TaskException ex){
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new com.stasevich.taskmanagementsystembackend.util.ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
        //return new ResponseEntity<>(new com.stasevich.taskmanagementsystembackend.util.ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
