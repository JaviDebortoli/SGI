package SGI.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectCreateDto (
        @NotBlank(message = "El nombre de proyecto es obligatorio.")
        @Size(min = 3, max = 30, message = "Debe tener de 3 a 30 caracteres.")
        String projectName,

        @NotBlank(message = "La descripción es obligatoria.")
        @Size(min = 3, max = 200, message = "Debe tener de 3 a 200 caracteres.")
        String projectDescription
) {}