package SGI.dto.issue;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

public record IssueAssignmentDto (
        @NotNull(message = "El usuario es requerido")
        UUID assigneeId
) {}