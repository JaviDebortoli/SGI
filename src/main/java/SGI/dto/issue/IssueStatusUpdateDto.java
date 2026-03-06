package SGI.dto.issue;

import SGI.domain.IssueStatus;
import jakarta.validation.constraints.NotNull;

public record IssueStatusUpdateDto (
        @NotNull(message = "El estado es obligatorio.")
        IssueStatus status
) {}