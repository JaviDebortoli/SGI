package SGI.controller;

import SGI.dto.user.UserCreateDto;
import SGI.dto.user.UserResponseDto;
import SGI.dto.user.UserUpdateDto;
import SGI.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers () {
        List<UserResponseDto> users = userService.getAllUsers();

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser (@Valid @RequestBody UserCreateDto userCreateDto) {
        UserResponseDto user = userService.createUser(userCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById (@PathVariable UUID userId) {
        UserResponseDto user = userService.getUserById(userId);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUserById (@PathVariable UUID userId,
                                                       @Valid @RequestBody UserUpdateDto userUpdateDto) {
        UserResponseDto user = userService.updateUser(userId, userUpdateDto);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById (@PathVariable UUID userId) {
        userService.deleteUser(userId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}