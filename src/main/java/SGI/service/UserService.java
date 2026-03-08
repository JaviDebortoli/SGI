package SGI.service;

import SGI.domain.User;
import SGI.dto.user.UserCreateDto;
import SGI.dto.user.UserResponseDto;
import SGI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void createUser (UserCreateDto userCreateDto) {
        // Crear el nuevo usuario
        User user = new User();
        user.setUserName(userCreateDto.userName());
        user.setEmail(userCreateDto.email());
        user.setPassword(userCreateDto.password());
        user.setEnabled(true);
        // Guardar usuario
        userRepository.save(user);
    }

    public List<UserResponseDto> getAllUsers() {
        // Retornar todos los usuarios
        return userRepository.findAll()
                .stream()
                .map(UserResponseDto::toUserResponseDto)
                .toList();
    }

    public UserResponseDto getUserById(UUID idUser) {
        // Buscar usuario
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el usuario con ID = " + idUser
                ));
        // Retornar usuario
        return UserResponseDto.toUserResponseDto(user);
    }
}