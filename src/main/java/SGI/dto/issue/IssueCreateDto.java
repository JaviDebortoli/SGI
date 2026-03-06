package SGI.dto.issue;

import SGI.domain.IssuePriority;
import SGI.domain.IssueType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record IssueCreateDto (
        @NotBlank(message = "El nombre de usuario es obligatorio.")
        @Size(min = 3, max = 20, message = "Debe tener de 3 a 20 caracteres.")
        String title,

        @NotBlank(message = "El nombre de usuario es obligatorio.")
        @Size(min = 3, max = 200, message = "Debe tener de 3 a 20 caracteres.")
        String issueDescription,

        @NotNull(message = "La prioridad es obligatoria.")
        IssuePriority priority,

        @NotNull(message = "El tipo es obligatorio.")
        IssueType type,

        @NotNull(message = "El proyecto es obligatorio.")
        UUID idProject,

        @NotNull(message = "El usuario es obligatorio.")
        UUID idUserReporter,

        @NotNull(message = "El usuario es obligatorio.")
        UUID idUserAssignee
) {}