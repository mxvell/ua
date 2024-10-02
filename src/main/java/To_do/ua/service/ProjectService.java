package To_do.ua.service;

import To_do.ua.dto.ProjectCreateDto;
import To_do.ua.dto.ProjectDto;
import To_do.ua.dto.UserDto;
import To_do.ua.entity.Project;
import To_do.ua.entity.User;
import To_do.ua.exceptions.ResourceNotFoundException;
import To_do.ua.mapping.ManagerMapping;
import To_do.ua.repository.ProjectRepository;
import To_do.ua.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(ManagerMapping::convertToProjectDto)
                .collect(toList());
    }

    public ProjectDto getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + id));
        return ManagerMapping.convertToProjectDto(project);
    }


    public ProjectDto createProject(ProjectCreateDto projectCreateDto) {
        UserDto userDto = userService.getUserById(projectCreateDto.getCreatedById());
        User user = ManagerMapping.convertToEntityUser(userDto);
        Project project = new Project(projectCreateDto.getName(), projectCreateDto.getDescription(), user);
        Project savedProject = projectRepository.save(project);
        return ManagerMapping.convertToProjectDto(savedProject);
    }

    public ProjectDto updateProject(Long id, ProjectCreateDto projectCreateDto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + id));
        project.setName(projectCreateDto.getName());
        project.setDescription(projectCreateDto.getDescription());

        Project updatedProject = projectRepository.save(project);
        return ManagerMapping.convertToProjectDto(updatedProject);
    }

    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
        projectRepository.delete(project);
    }

    public void addNewUsersToProject(Long projectId, List<Long> userIds) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + projectId));

        List<User> usersToAdd = userRepository.findAllById(userIds);

        if (usersToAdd.size() != userIds.size()) {
            Set<Long> foundUserIds = usersToAdd.stream()
                    .map(User::getId).collect(Collectors.toSet());
            List<Long> notFoundUserIds = userIds.stream()
                    .filter(id -> !foundUserIds.contains(id))
                    .toList();
            throw new ResourceNotFoundException("Users not found ids: " + notFoundUserIds);
        }

        for (User user : usersToAdd) {
            if (!project.getUsers().contains(user)) {
                project.getUsers().add(user);
            }
        }
        projectRepository.save(project);
    }

    public void deleteUsersFromProject(Long projectId, List<Long> userIds) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + projectId));

        List<User> usersToDelete = userRepository.findAllById(userIds);
        if (usersToDelete.size() != userIds.size()) {
            Set<Long> foundUserIds = usersToDelete.stream()
                    .map(User::getId).collect(Collectors.toSet());
            List<Long> notFoundUserIds = userIds.stream()
                    .filter(id -> !foundUserIds.contains(id))
                    .toList();
            throw new ResourceNotFoundException("Users not found ids: " + notFoundUserIds);
        }
        project.getUsers().removeAll(usersToDelete);
        projectRepository.save(project);
    }

    public List<UserDto> getProjectParticipants(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + projectId));
        return project.getUsers().stream()
                .map(ManagerMapping::convertToUserDto)
                .collect(toList());
    }

    public ProjectDto addParticipantToProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + projectId));
        UserDto userDto = userService.getUserById(userId);
        User user = ManagerMapping.convertToEntityUser(userDto);
        project.addUser(user);
        Project updatetProject = projectRepository.save(project);
        return ManagerMapping.convertToProjectDto(updatetProject);
    }


    public ProjectDto removeParticipantFromProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + projectId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        if (project.getUsers().contains(user)) {
            project.removeUser(user);
            user.removeProject(project);
            Project updatedProject = projectRepository.save(project);
            if (user.getProjects().isEmpty()) {
                userRepository.delete(user);
            } else {
                userRepository.save(user);
            }
            return ManagerMapping.convertToProjectDto(updatedProject);
        } else {
            throw new ResourceNotFoundException("User is not a participant in this project" + userId);
        }
    }

    public Optional<ProjectDto> findProjectByName(String name) {
        return projectRepository.findByName(name).map(ManagerMapping::convertToProjectDto);
    }


    public List<ProjectDto> getMyProjects(Long userId) {
        User currentUser = userService.getCurrentUser(userId);
        List<Project> userProjects = projectRepository.findByUsersContaining(currentUser);
        return ManagerMapping.convertToProjectDtoList(userProjects);
    }


}
























