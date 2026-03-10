package SGI.dto.project_member;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProjectMemberUpdateDto (
        @NotNull(message = "El rol es obligatorio.")
        UUID idRole
) {}