package To_do.ua.controller;

import To_do.ua.dto.TaskCreateDto;
import To_do.ua.dto.TaskResponseDto;
import To_do.ua.dto.TaskUpdateDto;
import To_do.ua.entity.TaskStatus;
import To_do.ua.exceptions.ResourceNotFoundException;
import To_do.ua.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        List<TaskResponseDto> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long taskId) {
        TaskResponseDto task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }
    // TO DO NOT WORKING
    @PostMapping("/{projectId}")
    public ResponseEntity<TaskResponseDto> createTask(@PathVariable Long projectId, @RequestBody TaskCreateDto taskCreateDtoDto) {
        TaskResponseDto createdTask = taskService.createTaskInProject(projectId, taskCreateDtoDto);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/project/{projectId}/{taskId}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long projectId,
                                                      @PathVariable Long taskId,
                                                      @RequestBody TaskUpdateDto taskUpdateDto) {
        TaskResponseDto updatedTask = taskService.updateTaskInProject(projectId, taskId, taskUpdateDto);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/projects/{projectId}/task/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long projectId, @PathVariable Long taskId) {
        taskService.deleteTaskFromProject(projectId, taskId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<List<TaskResponseDto>> getTasksByProject(@PathVariable Long projectId) {
        List<TaskResponseDto> tasks = taskService.getTasksByProject(projectId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<TaskResponseDto> getTaskByIdInProject(@PathVariable Long projectId, @PathVariable Long taskId) {
        TaskResponseDto task = taskService.getTaskById(taskId);
        if (!task.getProjectId().equals(projectId)) {
            throw new ResourceNotFoundException("Task does not belong to the specified project");
        }
        return ResponseEntity.ok(task);
    }

    @GetMapping("/projects/{projectId}/tasks/status/{status}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByStatusInProject(@PathVariable Long projectId, @PathVariable TaskStatus status) {
        List<TaskResponseDto> tasks = taskService.getTaskByStatus(status)
                .stream()
                .filter(task -> task.getProjectId().equals(projectId))
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<TaskResponseDto> assignTask(@PathVariable Long taskId, @PathVariable Long userId ){
        TaskResponseDto updateTask = taskService.assignTask(taskId, userId);
        return ResponseEntity.ok(updateTask);
    }

    @GetMapping("/assigned/{userId}")
    public ResponseEntity<List<TaskResponseDto>> getAssignedTasks(@PathVariable Long userId) {
       List<TaskResponseDto> tasks = taskService.getTaskByAssignedUser(userId);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
}
