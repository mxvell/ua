package To_do.ua.dto;

import To_do.ua.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskCreateDto {
    @NotNull
    private Long projectId;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    private Long assignedToId;
    @NotNull
    private Long createdById;
    @NotBlank
    private TaskStatus status;
}
