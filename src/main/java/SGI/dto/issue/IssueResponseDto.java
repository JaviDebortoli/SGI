package SGI.dto.issue;

import SGI.domain.Issue;

import java.util.UUID;

public record IssueResponseDto (
        UUID idIssue,
        String title,
        String issueDescription,
        String priority,
        String status,
        String type,
        String projectName,
        String userReporterName,
        String userAssigneeName
) {
    public static IssueResponseDto toIssueResponseDto (Issue issue) {
        return new IssueResponseDto(
                issue.getIdIssue(),
                issue.getTitle(),
                issue.getIssueDescription(),
                issue.getPriority().name(),
                issue.getStatus().name(),
                issue.getType().name(),
                issue.getProject().getProjectName(),
                issue.getReporter().getUserName(),
                issue.getAssignee().getUserName()
        );
    }
}