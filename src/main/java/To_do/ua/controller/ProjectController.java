package To_do.ua.controller;

import To_do.ua.dto.ProjectCreateDto;
import To_do.ua.dto.ProjectDto;
import To_do.ua.dto.UserCreateDto;
import To_do.ua.dto.UserDto;
import To_do.ua.entity.Project;
import To_do.ua.exceptions.ResourceNotFoundException;
import To_do.ua.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public List<ProjectDto> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ProjectDto getProjectById(@PathVariable Long id) {
        ProjectDto project = projectService.getProjectById(id);
        return ResponseEntity.ok(project).getBody();
    }

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@Valid @RequestBody ProjectCreateDto projectDto) {
        ProjectDto createProject = projectService.createProject(projectDto);
        return new ResponseEntity<>(createProject, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectCreateDto projectDto) {
        ProjectDto updateProject = projectService.updateProject(id, projectDto);
        return new ResponseEntity<>(updateProject, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return new  ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping("/{projectId}/users")
    public ResponseEntity<String> addUserToProject(@PathVariable Long projectId, @Valid @RequestBody List<Long> userIds) {
        try {
            projectService.addNewUsersToProject(projectId,userIds);
            return ResponseEntity.ok("Users added successfully to the project.");
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @DeleteMapping("/{projectId}/users")
    public ResponseEntity<Void> removeUsersFromProject(@PathVariable Long projectId, @Valid @RequestBody List<Long> userIds) {
        try {
            projectService.deleteUsersFromProject(projectId, userIds);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<UserDto>> getUsersFromProject(@PathVariable Long id) {
        List<UserDto> participants = projectService.getProjectParticipants(id);
        return new ResponseEntity<>(participants, HttpStatus.OK);
    }

    @PostMapping("/{projectId}/participant/{userId}")
    public ResponseEntity<ProjectDto> addParticipantToProject(@PathVariable Long projectId, @PathVariable Long userId) {
        ProjectDto updatedProject = projectService.addParticipantToProject(projectId, userId);
        return new ResponseEntity<>(updatedProject, HttpStatus.CREATED);
    }

    @DeleteMapping("/{projectId}/participant/{userId}")
    public ResponseEntity<ProjectDto> removeParticipantFromProject(@PathVariable Long projectId, @PathVariable Long userId) {
        ProjectDto updatedProject = projectService.removeParticipantFromProject(projectId, userId);
        return new ResponseEntity<>(updatedProject, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/name-project/{name}")
    public ResponseEntity<ProjectDto> searchProjectByInputName(@PathVariable String name) {
       return projectService.findProjectByName(name)
               .map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/my/{userId}")
    public ResponseEntity<List<ProjectDto>> getMyProjects(@PathVariable Long userId) {
        List<ProjectDto> myProjects = projectService.getMyProjects(userId);
        return ResponseEntity.ok(myProjects);
    }
}
