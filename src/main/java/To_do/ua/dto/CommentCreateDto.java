package To_do.ua.dto;

import To_do.ua.entity.User;
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
public class CommentCreateDto {
    @NotBlank(message = "Content cannot be blank")
    private String content;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

}
