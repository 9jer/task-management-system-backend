package com.stasevich.taskmanagementsystembackend.user.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Roles")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
