package com.stasevich.taskmanagementsystembackend.task.services;

import com.stasevich.taskmanagementsystembackend.task.models.Task;
import com.stasevich.taskmanagementsystembackend.task.models.TaskPriority;
import com.stasevich.taskmanagementsystembackend.task.models.TaskStatus;
import com.stasevich.taskmanagementsystembackend.task.repositories.TaskRepository;
import com.stasevich.taskmanagementsystembackend.task.util.TaskException;
import com.stasevich.taskmanagementsystembackend.user.models.User;
import com.stasevich.taskmanagementsystembackend.user.repositories.UserRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public List<Task> findAllTask(){
        return taskRepository.findAll();
    }

    @Override
    public Task findTaskById(Long id){
        return taskRepository.findById(id).orElseThrow(()->new TaskException("Task not found!"));
    }

    @Override
    @Transactional
    public Task createNewTask(Task task){

 /*       CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userDetails.getUser();*/
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(principal.toString());
        Optional<User> currentUser = userRepository.findByEmail(principal.toString());

        currentUser.ifPresent(task::setAuthor);

/*        List<User> assignees = userRepository.findAllById(taskToSave.getAssigneeIds());
        task.setAssignees(assignees);*/
        task.setCreatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public Task updateTaskStatus(Long id, TaskStatus status){
        return taskRepository.findById(id).map(task -> {
            task.setStatus(status);
            task.setUpdatedAt(LocalDateTime.now());
            return taskRepository.save(task);
        }).orElseThrow(() -> new TaskException("Task not found!"));
    }

    @Override
    @Transactional
    public Task updateTaskPriority(Long id, TaskPriority priority){
        return taskRepository.findById(id).map(task -> {
            task.setPriority(priority);
            task.setUpdatedAt(LocalDateTime.now());
            return taskRepository.save(task);
        }).orElseThrow(() -> new TaskException("Task not found!"));
    }

    @Override
    @Transactional
    public Task updateTaskAssignees(Long id, List<Long> assigneesIds){
        return taskRepository.findById(id).map(task -> {
            List<User> assignees = userRepository.findAllById(assigneesIds);
            if(assignees.isEmpty()){
                throw new TaskException("No valid users found for assignment!");
            }
            task.setAssignees(assignees);
            task.setUpdatedAt(LocalDateTime.now());
            return taskRepository.save(task);
        }).orElseThrow(() -> new TaskException("Task not found!"));
    }

    @Override
    @Transactional
    public Task updateTask(Long id, Task updatedTask){
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(updatedTask.getTitle());
                    task.setDescription(updatedTask.getDescription());
                    task.setStatus(updatedTask.getStatus());
                    task.setPriority(updatedTask.getPriority());
                    task.setUpdatedAt(LocalDateTime.now());
                    return taskRepository.save(task);
                }).orElseThrow(() -> new TaskException("Task not found!"));
    }

    @Override
    public Page<Task> findTasksByAuthor(Long id, Pageable pageable){
        return taskRepository.findByAuthorId(id, pageable);
    }

    @Override
    public Page<Task> findTaskByAssignee(Long id, Pageable pageable){
        return taskRepository.findByAssigneesId(id, pageable);
    }

    @Override
    @Transactional
    public void deleteTaskById(Long id){
        if(findTaskById(id) == null){
            throw new TaskException("Task not found!");
        }
        taskRepository.deleteById(id);
    }

    @Override
    public Page<Task> findTasksByFilters(Long authorId, Long assigneeId, TaskStatus status, TaskPriority priority, Pageable pageable) {
        Specification<Task> spec = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(authorId != null) {
                predicates.add(criteriaBuilder.equal(root.get("author").get("id"), authorId));
            }
            if(assigneeId != null) {
                predicates.add(criteriaBuilder.equal(root.get("assignees").get("id"), assigneeId));
            }
            if(status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if(priority != null) {
                predicates.add(criteriaBuilder.equal(root.get("priority"), priority));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        });

        return taskRepository.findAll(spec, pageable);
    }
}
