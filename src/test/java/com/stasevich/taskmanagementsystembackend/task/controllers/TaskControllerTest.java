package com.stasevich.taskmanagementsystembackend.task.controllers;

import com.stasevich.taskmanagementsystembackend.task.dto.SaveTaskDto;
import com.stasevich.taskmanagementsystembackend.task.dto.TaskDto;
import com.stasevich.taskmanagementsystembackend.task.models.Task;
import com.stasevich.taskmanagementsystembackend.task.models.TaskPriority;
import com.stasevich.taskmanagementsystembackend.task.models.TaskStatus;
import com.stasevich.taskmanagementsystembackend.task.services.TaskService;
import com.stasevich.taskmanagementsystembackend.task.util.TaskException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private TaskController taskController;

    private Task task;
    private TaskDto taskDto;
    private SaveTaskDto saveTaskDto;

    @BeforeEach
    void setUp() {
        task = Task.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .status(TaskStatus.IN_PROGRESS)
                .priority(TaskPriority.MEDIUM)
                .build();

        taskDto = TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .build();

        saveTaskDto = SaveTaskDto.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .build();

        //when(modelMapper.map(any(Task.class), eq(TaskDto.class))).thenReturn(taskDto);
        //when(modelMapper.map(any(SaveTaskDto.class), eq(Task.class))).thenReturn(task);
    }

    @Test
    void findAllTasks_ReturnsValidResponseEntity() {
        // 3 части: дано (given) - вызов(when) - тогда (then)
        when(modelMapper.map(any(Task.class), eq(TaskDto.class))).thenReturn(taskDto);
        // given
        when(taskService.findAllTask()).thenReturn(List.of(task));

        // when
        var response = taskController.findAllTasks();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTasks().size());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(List.of(taskDto), response.getBody().getTasks());

        verify(taskService, times(1)).findAllTask();
    }

    @Test
    void createTask_BindingResultHasNotErrors_ReturnsValidResponseEntity() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(modelMapper.map(any(SaveTaskDto.class), eq(Task.class))).thenReturn(task);
        when(taskService.createNewTask(task)).thenReturn(task);
        when(modelMapper.map(any(Task.class), eq(TaskDto.class))).thenReturn(taskDto);

        var responseEntity = taskController.createTask(saveTaskDto, bindingResult);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertNotNull(Objects.requireNonNull(responseEntity.getBody()).getId());
        assertEquals("Test Task", responseEntity.getBody().getTitle());

        verify(taskService, times(1)).createNewTask(any(Task.class));
    }

    @Test
    void createTask_BindingResultHasErrors_ReturnsBadRequest() {
        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(TaskException.class, () -> taskController.createTask(saveTaskDto, bindingResult));
        verify(taskService, never()).createNewTask(any());

    }

    @Test
    void updateTask_BindingResultHasNotErrors_ReturnsValidResponseEntity() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(taskService.updateTask(1L, task)).thenReturn(task);
        when(modelMapper.map(any(SaveTaskDto.class), eq(Task.class))).thenReturn(task);
        when(modelMapper.map(any(Task.class), eq(TaskDto.class))).thenReturn(taskDto);

        var responseEntity = taskController.updateTask(1L, saveTaskDto, bindingResult);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(responseEntity.getBody(), taskDto);

        verify(taskService, times(1)).updateTask(anyLong(), any(Task.class));
    }

    @Test
    void updateTask_BindingResultHasErrors_ReturnsBadRequest() {
        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(TaskException.class, () -> taskController.updateTask(1L, saveTaskDto, bindingResult));

        verify(taskService, never()).updateTask(anyLong(), any(Task.class));
    }

    @Test
    void findTaskById_TaskExists_ReturnsValidResponseEntity() {
        when(modelMapper.map(any(Task.class), eq(TaskDto.class))).thenReturn(taskDto);
        when(taskService.findTaskById(1L)).thenReturn(task);

        var responseEntity = taskController.findTaskById(1L);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(taskDto, responseEntity.getBody());

        verify(taskService, times(1)).findTaskById(anyLong());
    }

    @Test
    void findTaskById_TaskDoesNotExist_ReturnsNotFound() {
        when(taskService.findTaskById(1L)).thenThrow(new TaskException("Task not found"));

        assertThrows(TaskException.class, () -> taskController.findTaskById(1L));

        verify(taskService, times(1)).findTaskById(1L);
    }

    @Test
    void deleteTaskById_TaskExists_ReturnsHttpStatusOk() {
        //when(taskService.findTaskById(1L)).thenReturn(task);
        doNothing().when(taskService).deleteTaskById(1L);

        var responseEntity = taskController.deleteTaskById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(taskService, times(1)).deleteTaskById(1L);
        //verify(taskService, times(1)).findTaskById(1L);
    }

    @Test
    void deleteTaskById_TaskDoesNotExist_ReturnsNotFound() {

    }
}