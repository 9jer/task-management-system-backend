package com.stasevich.taskmanagementsystembackend.user.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.stasevich.taskmanagementsystembackend.comment.models.Comment;
import com.stasevich.taskmanagementsystembackend.task.models.Task;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "Users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    @NotEmpty(message = "Username should not be empty!")
    private String username;

    @Column(name = "email")
    @NotEmpty(message = "Email should be not empty!")
    @Email(message = "Incorrect email!")
    private String email;

    @Column(name = "password")
    @NotEmpty(message = "Password should be not empty!")
    private String password;

    @Column(name = "name")
    @NotEmpty(message = "Name should be not empty!")
    private String name;

    @Column(name = "phone")
    @NotEmpty(message = "Phone number should not be empty")
    private String phone;

    @ManyToMany
    @JoinTable(
            name = "Users_Roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles;

    @ManyToMany(mappedBy = "assignees")
    private List<Task> assignedTasks;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Task> authorTask;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
