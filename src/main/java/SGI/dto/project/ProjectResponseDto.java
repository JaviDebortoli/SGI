package SGI.dto.project;

import SGI.domain.Project;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectResponseDto (
        UUID idProject,
        String projectName,
        String projectDescription,
        LocalDateTime createdAt,
        Boolean active
) {
    public static  ProjectResponseDto toProjectResponseDto(Project project) {
        return new ProjectResponseDto(
                project.getIdProject(),
                project.getProjectName(),
                project.getProjectDescription(),
                project.getCreatedAt(),
                project.isActive()
        );
    }
}