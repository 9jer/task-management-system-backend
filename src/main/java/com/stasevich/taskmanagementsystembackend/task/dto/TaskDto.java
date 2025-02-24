package com.stasevich.taskmanagementsystembackend.task.dto;

import com.stasevich.taskmanagementsystembackend.comment.dto.CommentDto;
import com.stasevich.taskmanagementsystembackend.task.models.TaskPriority;
import com.stasevich.taskmanagementsystembackend.task.models.TaskStatus;
import com.stasevich.taskmanagementsystembackend.user.dto.UserDto;
import com.stasevich.taskmanagementsystembackend.user.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TaskDto {
    private Long id;

    @NotEmpty(message = "Title should not be empty!")
    private String title;

    @NotEmpty(message = "Description should not be empty!")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status should not be empty!")
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Priority should not be empty!")
    private TaskPriority priority;

    private UserDto author;

    private List<UserDto> assignees;

    private List<CommentDto> comments;
}
