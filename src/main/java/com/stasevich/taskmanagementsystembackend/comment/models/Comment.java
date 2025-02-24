package com.stasevich.taskmanagementsystembackend.comment.models;

import com.stasevich.taskmanagementsystembackend.task.models.Task;
import com.stasevich.taskmanagementsystembackend.user.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Comments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content")
    @NotEmpty(message = "Content should not be empty!")
    private String content;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
