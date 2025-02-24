package com.stasevich.taskmanagementsystembackend.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CommentsResponse {
    private List<CommentDto> comments;
}
