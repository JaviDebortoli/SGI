package SGI.dto.issue;

import SGI.domain.IssuePriority;
import SGI.domain.IssueType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record IssueCreateDto (
        @NotBlank(message = "El titulo es obligatorio.")
        @Size(min = 3, max = 100, message = "Debe tener de 3 a 100 caracteres.")
        String title,

        @NotBlank(message = "La descripción es obligatoria.")
        @Size(min = 3, max = 200, message = "Debe tener de 3 a 200 caracteres.")
        String issueDescription,

        @NotNull(message = "La prioridad es obligatoria.")
        IssuePriority priority,

        @NotNull(message = "El tipo es obligatorio.")
        IssueType type,

        @NotNull(message = "El usuario es obligatorio.")
        UUID idUserReporter,

        @NotNull(message = "El usuario es obligatorio.")
        UUID idUserAssignee
) {}