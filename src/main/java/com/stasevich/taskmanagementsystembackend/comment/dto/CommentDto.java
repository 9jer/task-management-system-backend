package com.stasevich.taskmanagementsystembackend.comment.dto;

import com.stasevich.taskmanagementsystembackend.comment.models.Comment;
import com.stasevich.taskmanagementsystembackend.user.models.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class CommentDto {
    private Long id;

    @NotEmpty(message = "Content should not be empty!")
    private String content;

    private String authorName;

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.authorName = comment.getAuthor().getName();
    }
}
