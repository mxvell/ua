package To_do.ua.dto;

import To_do.ua.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto{
    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;


}
