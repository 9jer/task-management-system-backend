package com.stasevich.taskmanagementsystembackend.comment.services;

import com.stasevich.taskmanagementsystembackend.comment.models.Comment;

import java.util.List;

public interface CommentService {
    Comment addCommentToTask(Long taskId, Comment comment);
    List<Comment> getCommentsByTask(Long taskId);
}
