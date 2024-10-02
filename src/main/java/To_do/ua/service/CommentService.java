package To_do.ua.service;

import To_do.ua.dto.CommentCreateDto;
import To_do.ua.dto.CommentDto;
import To_do.ua.entity.Comment;
import To_do.ua.exceptions.ResourceNotFoundException;
import To_do.ua.mapping.ManagerMapping;
import To_do.ua.repository.CommentRepository;
import To_do.ua.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskService taskService;

    public List<CommentDto> findAll() {
        return commentRepository.findAll().stream()
                .map(ManagerMapping::convertToCommentDto)
                .toList();
    }

    public CommentDto findById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        return ManagerMapping.convertToCommentDto(comment);
    }

    public CommentDto addCommentToTask(Long taskId, CommentCreateDto commentCreateDto) {
        taskService.getTaskById(taskId);

        Comment comment = ManagerMapping.convertToEntityComment(commentCreateDto);
        comment.setCreatedAt(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);

        return ManagerMapping.convertToCommentDto(savedComment);

    }

    public List<CommentDto> getCommentsByTaskId(Long taskId) {
        return commentRepository.findByTaskId(taskId).stream()
                .map(ManagerMapping::convertToCommentDto)
                .toList();
    }

    public CommentDto updateComment(Long id, CommentCreateDto updateCommentDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
        comment.setContent(updateCommentDto.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);
        return ManagerMapping.convertToCommentDto(savedComment);
    }

    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Comment not found with id: " + id);
        }
        commentRepository.deleteById(id);
    }
}
