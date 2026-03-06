package SGI.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateDto (
        @NotBlank(message = "El nombre de usuario es obligatorio.")
        @Size(min = 3, max = 100, message = "Debe tener de 3 a 100 caracteres.")
        String userName,

        @NotBlank(message = "El email es obligatorio.")
        @Email(message = "Debe ser un email válido.")
        String email,

        @NotBlank(message = "Una contraseña es obligatoria.")
        @Size(
                min = 4,
                max = 30,
                message = "La contraseña debe de 4 a 30 caracteres"
        )
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
                message = "La contraseña debe contener una mayúscula, una minúscula, un número y un caracter especial"
        )
        String password,

        Boolean enabled
) {}