package SGI.service;

import SGI.domain.User;
import SGI.dto.user.UserCreateDto;
import SGI.dto.user.UserResponseDto;
import SGI.dto.user.UserUpdateDto;
import SGI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto createUser (UserCreateDto userCreateDto) {
        // Verificar nombre de usuario
        if(userRepository.existsByUserName(userCreateDto.userName())){
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }
        // Verificar email
        if(userRepository.existsByEmail(userCreateDto.email())){
            throw new IllegalArgumentException("El email ya está registrado");
        }
        // Crear el nuevo usuario
        User user = new User();
        user.setUserName(userCreateDto.userName());
        user.setEmail(userCreateDto.email());
        user.setPassword(passwordEncoder.encode(userCreateDto.password()));
        user.setEnabled(true);
        // Guardar usuario
        userRepository.save(user);
        // Retornar usuario creado
        return UserResponseDto.toUserResponseDto(user);
    }

    public List<UserResponseDto> getAllUsers() {
        // Retornar todos los usuarios
        return userRepository.findByEnabledTrue()
                .stream()
                .map(UserResponseDto::toUserResponseDto)
                .toList();
    }

    public UserResponseDto getUserById(UUID idUser) {
        // Buscar usuario
        User user = userRepository.findByIdUserAndEnabledTrue(idUser)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el usuario con ID = " + idUser
                ));
        // Retornar usuario
        return UserResponseDto.toUserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateUser(UUID idUser, UserUpdateDto userUpdateDto) {
        // Verificar nombre de usuario
        if(userRepository.existsByUserName(userUpdateDto.userName())){
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }
        // Verificar email
        if(userRepository.existsByEmail(userUpdateDto.email())){
            throw new IllegalArgumentException("El email ya está registrado");
        }
        // Buscar usuario
        User user = userRepository.findByIdUserAndEnabledTrue(idUser)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el usuario con ID = " + idUser
                ));
        // Actualizar usuario
        user.setUserName(userUpdateDto.userName());
        user.setEmail(userUpdateDto.email());
        user.setPassword(passwordEncoder.encode(userUpdateDto.password()));
        // Retornar usuario actualizado
        return UserResponseDto.toUserResponseDto(user);
    }

    @Transactional
    public void deleteUser(UUID idUser) {
        // Buscar usuario
        User user = userRepository.findByIdUserAndEnabledTrue(idUser)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el usuario con ID = " + idUser
                ));
        // Desactivar usuario
        user.setEnabled(false);
    }
}
//ffffffffffffeeeeeeeeeeeeerrrrrrrrr