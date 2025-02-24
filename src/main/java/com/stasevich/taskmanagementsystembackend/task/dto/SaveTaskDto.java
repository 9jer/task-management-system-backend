package com.stasevich.taskmanagementsystembackend.task.dto;

import com.stasevich.taskmanagementsystembackend.task.models.TaskPriority;
import com.stasevich.taskmanagementsystembackend.task.models.TaskStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class SaveTaskDto {

    @NotEmpty(message = "Title should not be empty!")
    private String title;

    @NotEmpty(message = "Description should not be empty!")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Priority should not be empty!")
    private TaskPriority priority;

}
