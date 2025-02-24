package com.stasevich.taskmanagementsystembackend.comment.controllers;

import com.stasevich.taskmanagementsystembackend.comment.dto.CommentDto;
import com.stasevich.taskmanagementsystembackend.comment.dto.CommentsResponse;
import com.stasevich.taskmanagementsystembackend.comment.dto.SaveCommentDto;
import com.stasevich.taskmanagementsystembackend.comment.models.Comment;
import com.stasevich.taskmanagementsystembackend.comment.services.CommentService;
import com.stasevich.taskmanagementsystembackend.comment.util.CommentException;
import com.stasevich.taskmanagementsystembackend.comment.util.ErrorsUtil;
import com.stasevich.taskmanagementsystembackend.util.ErrorResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks/{taskId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final ModelMapper modelMapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CommentDto> addComment(@PathVariable(name = "taskId") Long taskId, @RequestBody @Valid SaveCommentDto saveCommentDto, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            ErrorsUtil.returnAllErrors(bindingResult);
        }

        return new ResponseEntity<>(convertCommentToCommentDto(commentService
                .addCommentToTask(taskId, convertSaveCommentDtoToComment(saveCommentDto))), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CommentsResponse> getComments(@PathVariable(name = "taskId") Long taskId) {
        //List<CommentDto> comments = commentService.getCommentsByTask(taskId).stream().map(CommentDto::new).toList();

        return new ResponseEntity<>(new CommentsResponse(commentService.getCommentsByTask(taskId)
                .stream().map(this::convertCommentToCommentDto).toList()), HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(CommentException ex){
        return new ResponseEntity<>(new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        ), HttpStatus.BAD_REQUEST);
    }

    private Comment convertSaveCommentDtoToComment(SaveCommentDto saveCommentDto) {
        return modelMapper.map(saveCommentDto, Comment.class);
    }

    private CommentDto convertCommentToCommentDto(Comment comment) {
        return modelMapper.map(comment, CommentDto.class);
    }
}
