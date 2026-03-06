package SGI.repository;

import SGI.domain.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StatusHistoryRepository extends JpaRepository<StatusHistory, UUID> {}