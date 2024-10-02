package To_do.ua.service;

import To_do.ua.dto.UserCreateDto;
import To_do.ua.dto.UserDto;
import To_do.ua.entity.User;
import To_do.ua.exceptions.ResourceNotFoundException;
import To_do.ua.exceptions.UserAlreadyExistsException;
import To_do.ua.mapping.ManagerMapping;
import To_do.ua.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(ManagerMapping::convertToUserDto)
                .collect(toList());
    }

    // We are using this method for project service class
    public User getCurrentUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        return ManagerMapping.convertToUserDto(user);
    }

    public UserDto createUser(UserCreateDto userCreateDto) {
        if (userRepository.existsByUsername(userCreateDto.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(userCreateDto.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        User user = new User();
        user.setUsername(userCreateDto.getUsername());
        user.setEmail(userCreateDto.getEmail());
        user.setPassword(userCreateDto.getPassword());

        User savedUser = userRepository.save(user);
        return ManagerMapping.convertToUserDto(savedUser);
    }

    public UserDto updateUser(Long id, UserCreateDto updateUserDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        if (!user.getUsername().equals(updateUserDto.getUsername()) &&
                userRepository.existsByUsername(updateUserDto.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (!user.getEmail().equals(updateUserDto.getEmail()) &&
                userRepository.existsByEmail(updateUserDto.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        user.setUsername(updateUserDto.getUsername());
        user.setEmail(updateUserDto.getEmail());
        if (updateUserDto.getPassword() != null && !updateUserDto.getPassword().isEmpty()) {
            user.setPassword(updateUserDto.getPassword());
        }
        User updatedUser = userRepository.save(user);
        return ManagerMapping.convertToUserDto(updatedUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id " + id);
        }
        userRepository.deleteById(id);
    }

    public Optional<UserDto> findUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username)
                .map(ManagerMapping::convertToUserDto)
                .orElseThrow(() -> new ResourceNotFoundException("This user does not exist with name " + username)));
    }

    public Optional<UserDto> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email)
                .map(ManagerMapping::convertToUserDto)
                .orElseThrow(() -> new ResourceNotFoundException("Try again, we can't find this email " + email)));
    }

}
