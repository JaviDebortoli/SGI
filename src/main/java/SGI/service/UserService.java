package SGI.service;

import SGI.domain.User;
import SGI.dto.user.UserCreateDto;
import SGI.dto.user.UserResponseDto;
import SGI.dto.user.UserUpdateDto;
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
        // Verificar unicidad de email y nombre de usuario
        IllegalArgumentException exception = checkUserNameAndEmail(userCreateDto.userName(), userCreateDto.email());
        if(exception != null){
            throw exception;
        }
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

    public void updateUser(UUID idUser, UserUpdateDto userUpdateDto) {
        // Verificar unicidad de email y nombre de usuario
        IllegalArgumentException exception = checkUserNameAndEmail(userUpdateDto.userName(), userUpdateDto.email());
        if(exception != null){
            throw exception;
        }
        // Buscar usuario
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el usuario con ID = " + idUser
                ));
        // Actualizar usuario
        user.setUserName(userUpdateDto.userName());
        user.setEmail(userUpdateDto.email());
        user.setPassword(userUpdateDto.password());
    }

    public void deleteUser(UUID idUser) {
        // Buscar usuario
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el usuario con ID = " + idUser
                ));
        // Desactivar usuario
        user.setEnabled(false);
    }

    private IllegalArgumentException checkUserNameAndEmail (String userName, String email) {
        // Verificar nombre de usuario
        if(userRepository.existsByUserName(userName)){
            return new IllegalArgumentException("El nombre de usuario ya existe");
        }
        // Verificar email
        if(userRepository.existsByEmail(email)){
            return new IllegalArgumentException("El email ya está registrado");
        }

        return null;
    }
}