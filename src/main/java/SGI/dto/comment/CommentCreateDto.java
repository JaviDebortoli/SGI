package SGI.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CommentCreateDto (
        @NotBlank(message = "El nombre de usuario es obligatorio.")
        @Size(min = 3, max = 200, message = "Debe tener entre 3 y 200 caracteres.")
        String content,

        @NotNull(message = "El usuario es obligatorio.")
        UUID idUser
) {}