package SGI.repository;

import SGI.domain.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StatusHistoryRepository extends JpaRepository<StatusHistory, UUID> {
    List<StatusHistory> findByIssueIdIssueOrderByUpdatedAtDesc(UUID issueId);
}