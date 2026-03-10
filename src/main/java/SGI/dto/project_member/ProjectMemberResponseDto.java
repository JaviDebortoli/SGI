package SGI.dto.project_member;

import SGI.domain.ProjectMember;

import java.util.UUID;

public record ProjectMemberResponseDto (
        UUID idProjectMember,
        String projectName,
        String userName,
        String roleName,
        Boolean active
) {
    public static  ProjectMemberResponseDto toProjectMemberResponseDto(ProjectMember projectMember) {
        return new ProjectMemberResponseDto (
                projectMember.getIdProjectMember(),
                projectMember.getProject().getProjectName(),
                projectMember.getUser().getUserName(),
                projectMember.getRole().getRoleName().name(),
                projectMember.isActive()
        );
    }
}