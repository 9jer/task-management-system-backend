package com.stasevich.taskmanagementsystembackend.comment.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SaveCommentDto {

    @NotEmpty(message = "Content should not be empty!")
    private String content;
}
