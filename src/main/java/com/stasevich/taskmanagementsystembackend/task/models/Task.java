package com.stasevich.taskmanagementsystembackend.task.models;

import com.stasevich.taskmanagementsystembackend.comment.models.Comment;
import com.stasevich.taskmanagementsystembackend.user.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Tasks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    @NotEmpty(message = "Title should not be empty!")
    private String title;

    @Column(name = "description")
    @NotEmpty(message = "Description should not be empty!")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status should not be empty!")
    @Column(name = "status")
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    @NotNull(message = "Priority should not be empty!")
    private TaskPriority priority;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @NotNull(message = "Author should not be empty!")
    private User author;

    @ManyToMany
    @JoinTable(
            name = "Tasks_Users",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> assignees;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comment> comments;
}
