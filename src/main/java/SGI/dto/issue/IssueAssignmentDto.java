package SGI.dto.issue;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record IssueAssignmentDto (
        @NotNull(message = "El usuario es requerido")
        UUID assigneeId
) {}