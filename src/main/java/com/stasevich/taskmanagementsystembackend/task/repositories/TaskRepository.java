package com.stasevich.taskmanagementsystembackend.task.repositories;

import com.stasevich.taskmanagementsystembackend.task.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    Page<Task> findByAuthorId(Long authorId, Pageable pageable);
    Page<Task> findByAssigneesId(Long assigneeId, Pageable pageable);
}
