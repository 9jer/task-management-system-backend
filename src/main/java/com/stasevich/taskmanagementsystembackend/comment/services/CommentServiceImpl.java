package com.stasevich.taskmanagementsystembackend.comment.services;

import com.stasevich.taskmanagementsystembackend.comment.models.Comment;
import com.stasevich.taskmanagementsystembackend.comment.repositories.CommentRepository;
import com.stasevich.taskmanagementsystembackend.comment.util.CommentException;
import com.stasevich.taskmanagementsystembackend.task.models.Task;
import com.stasevich.taskmanagementsystembackend.task.repositories.TaskRepository;
import com.stasevich.taskmanagementsystembackend.user.models.User;
import com.stasevich.taskmanagementsystembackend.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public Comment addCommentToTask(Long taskId, Comment comment){
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new CommentException("Task not found"));

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> currentUser = userRepository.findByEmail(principal.toString());

        if(currentUser.isEmpty()){
            throw new CommentException("User not found");
        }

        comment.setTask(task);
        comment.setAuthor(currentUser.get());
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsByTask(Long taskId){
        return commentRepository.findByTaskId(taskId);
    }
}
