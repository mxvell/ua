package To_do.ua.mapping;

import To_do.ua.dto.*;
import To_do.ua.entity.Comment;
import To_do.ua.entity.Project;
import To_do.ua.entity.Task;
import To_do.ua.entity.User;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@Component
public class ManagerMapping {

    private static final ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
        modelMapper.createTypeMap(User.class, UserDto.class);
        modelMapper.createTypeMap(UserDto.class, User.class);
        modelMapper.createTypeMap(Project.class, ProjectDto.class);
        modelMapper.createTypeMap(ProjectDto.class, Project.class);
        modelMapper.createTypeMap(Task.class, TaskResponseDto.class);
        modelMapper.createTypeMap(Comment.class, CommentDto.class);

    }


    public static UserDto convertToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public static User convertToEntityUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }


    public static ProjectDto convertToProjectDto(Project project) {
        return modelMapper.map(project, ProjectDto.class);
    }

    public static Project convertToEntityProject(ProjectDto projectDto) {
        return modelMapper.map(projectDto, Project.class);
    }

    public static List<ProjectDto> convertToProjectDtoList(List<Project> projects) {
        return projects.stream()
                .map(project -> modelMapper.map(project, ProjectDto.class))
                .collect(toList());
    }

    public static TaskResponseDto convertToTaskDto(Task task) {
        return modelMapper.map(task, TaskResponseDto.class);
    }

    public static Task convertToEntityTask(TaskResponseDto taskResponseDto) {
        return modelMapper.map(taskResponseDto, Task.class);
    }

    public static List<TaskResponseDto> convertToTaskDtoList(List<Task> tasks) {
        return tasks.stream()
                .map(ManagerMapping::convertToTaskDto)
                .collect(Collectors.toList());
    }

    public static List<Task> convertToTaskList(List<TaskResponseDto> taskResponseDtos) {
        return taskResponseDtos.stream()
                .map(ManagerMapping::convertToEntityTask)
                .collect(Collectors.toList());
    }

    public static CommentDto convertToCommentDto(Comment comments) {
        return modelMapper.map(comments, CommentDto.class);
    }

    public static Comment convertToEntityComment(CommentCreateDto commentCreateDto) {
        return modelMapper.map(commentCreateDto, Comment.class);
    }
}
