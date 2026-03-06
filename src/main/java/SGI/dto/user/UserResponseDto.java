package SGI.dto.user;

import SGI.domain.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDto (
        UUID idUser,
        String userName,
        String email,
        Boolean enabled,
        LocalDateTime createdAt
) {
    public static UserResponseDto toUserResponseDto(User user) {
        return new UserResponseDto(
                user.getIdUser(),
                user.getUserName(),
                user.getEmail(),
                user.isEnabled(),
                user.getCreatedAt()
        );
    }
}