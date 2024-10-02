package To_do.ua.service;

import To_do.ua.dto.TaskCreateDto;
import To_do.ua.dto.TaskResponseDto;
import To_do.ua.dto.TaskUpdateDto;
import To_do.ua.entity.Project;
import To_do.ua.entity.Task;
import To_do.ua.entity.TaskStatus;
import To_do.ua.entity.User;
import To_do.ua.exceptions.ResourceNotFoundException;
import To_do.ua.mapping.ManagerMapping;
import To_do.ua.repository.ProjectRepository;
import To_do.ua.repository.TaskRepository;
import To_do.ua.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public List<TaskResponseDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return ManagerMapping.convertToTaskDtoList(tasks);
    }


    public TaskResponseDto getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return ManagerMapping.convertToTaskDto(task);
    }

    public List<TaskResponseDto> getTaskByAssignedUser(Long userId) {
        List<Task> tasks = taskRepository.findByAssignedToId(userId);
        return ManagerMapping.convertToTaskDtoList(tasks);
    }

    public List<TaskResponseDto> getTaskByStatus(TaskStatus status) {
        List<Task> tasks = taskRepository.findByStatus(status);
        return ManagerMapping.convertToTaskDtoList(tasks);
    }

    public TaskResponseDto createTaskInProject(Long projectId, TaskCreateDto taskCreateDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        User assignedTo = userRepository.findById(taskCreateDto.getAssignedToId())
                .orElseThrow(() -> new ResourceNotFoundException("Assigned user not found"));

        User createdBy = userRepository.findById(taskCreateDto.getCreatedById())
                .orElseThrow(() -> new ResourceNotFoundException("Creator user not found"));

//todo можна зробити конвертацію в ентіті клас, щоб вручну не сетати, окрім localDateTime це вручну
        Task task = new Task();

        task.setProject(project);
        task.setTitle(taskCreateDto.getTitle());
        task.setDescription(taskCreateDto.getDescription());
        task.setAssignedTo(assignedTo);
        task.setCreatedBy(createdBy);
        task.setStatus(TaskStatus.TO_DO);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        Task savedTask = taskRepository.save(task);
        return ManagerMapping.convertToTaskDto(savedTask);
    }

    public TaskResponseDto updateTaskInProject(Long projectId, Long taskId, TaskUpdateDto taskUpdateDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!existingTask.getProject().getId().equals(projectId)) {
            throw new ResourceNotFoundException("Task doesn't belong to the specified project");
        }

        existingTask.setTitle(taskUpdateDto.getTitle());
        existingTask.setDescription(taskUpdateDto.getDescription());

        if (taskUpdateDto.getStatus() != null) {
            existingTask.setStatus(taskUpdateDto.getStatus());
        }

        if (taskUpdateDto.getAssignedToId() != null) {
            User assignedUser = userRepository.findById(taskUpdateDto.getAssignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assigned user not found"));
            existingTask.setAssignedTo(assignedUser);
        }

        existingTask.setUpdatedAt(LocalDateTime.now());

        Task updatedTask = taskRepository.save(existingTask);
        return ManagerMapping.convertToTaskDto(updatedTask);
    }

    public void deleteTaskFromProject(Long projectId, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!task.getProject().getId().equals(projectId)) {
            throw new ResourceNotFoundException("Task doesn't belong to the specified project");
        }
        taskRepository.delete(task);
    }

    public List<TaskResponseDto> getTasksByProject(Long projectId) {
        projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        List<Task> tasks = taskRepository.findByProjectId(projectId);
        return ManagerMapping.convertToTaskDtoList(tasks);
    }

    public TaskResponseDto assignTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        task.setAssignedTo(user);
        task.setUpdatedAt(LocalDateTime.now());
        Task savedTask = taskRepository.save(task);
        return ManagerMapping.convertToTaskDto(savedTask);
    }
}





















