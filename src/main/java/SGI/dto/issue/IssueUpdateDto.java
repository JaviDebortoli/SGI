package SGI.dto.issue;

import SGI.domain.IssuePriority;
import SGI.domain.IssueStatus;
import SGI.domain.IssueType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record IssueUpdateDto (
        @NotBlank(message = "El titulo de la incidencia es obligatoria.")
        @Size(min = 3, max = 100, message = "Debe tener de 3 a 100 caracteres.")
        String title,

        @NotNull(message = "La prioridad es obligatoria.")
        IssuePriority priority,

        @NotNull(message = "El tipo es obligatorio.")
        IssueType type
) {}