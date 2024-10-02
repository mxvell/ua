package To_do.ua.dto;

import To_do.ua.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskResponseDto {
    private Long id;
    private Long projectId;
    private String title;
    private String description;
    private Long assignedToId;
    private Long createdById;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private TaskStatus status;
}
