package SGI.repository;

import SGI.domain.Issue;
import SGI.domain.IssuePriority;
import SGI.domain.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IssueRepository extends JpaRepository<Issue, UUID> {
    boolean existsByIdIssue(UUID issueId);
    Optional<Issue> findByIdIssue(UUID issueId);
    List<Issue> findByProjectIdProject(UUID projectId);
    List<Issue> findByStatus(IssueStatus status);
    List<Issue> findByPriority(IssuePriority priority);
}