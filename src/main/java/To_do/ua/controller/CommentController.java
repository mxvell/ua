package To_do.ua.controller;

import To_do.ua.dto.CommentCreateDto;
import To_do.ua.dto.CommentDto;
import To_do.ua.exceptions.ResourceNotFoundException;
import To_do.ua.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getAllComments() {
        return commentService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long id) {
        CommentDto commentDto = commentService.findById(id);
        if (commentDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(commentDto);
    }

    @GetMapping("/task/{taskId}")
    public List<CommentDto> getAllCommentsByTaskId(@PathVariable Long taskId) {
        return commentService.getCommentsByTaskId(taskId);
    }

    @PostMapping("/task/{taskId}")
    public ResponseEntity<CommentDto> addCommentToTask(@PathVariable Long taskId, @Valid @RequestBody CommentCreateDto commentCreateDto) {
        try {
            CommentDto comment = commentService.addCommentToTask(taskId,commentCreateDto);
            return ResponseEntity.ok(comment);
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long id, @Valid @RequestBody CommentCreateDto updateCommentDTO) {
        try {
            CommentDto updatedComment = commentService.updateComment(id, updateCommentDTO);
            return ResponseEntity.ok(updatedComment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
