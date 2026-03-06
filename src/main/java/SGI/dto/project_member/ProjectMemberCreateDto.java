package SGI.dto.project_member;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProjectMemberCreateDto (
        @NotNull(message = "El proyecto es obligatorio.")
        UUID idProject,

        @NotNull(message = "El usuario es obligatorio.")
        UUID idUser,

        @NotNull(message = "El rol es obligatorio.")
        UUID idRole
){}