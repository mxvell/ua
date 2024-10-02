package To_do.ua.dto;

import To_do.ua.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskUpdateDto {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private TaskStatus status;
    @NotBlank
    private Long assignedToId;
}
