package SGI.dto.status_history;

import SGI.domain.StatusHistory;

import java.time.LocalDateTime;
import java.util.UUID;

public record StatusHistoryResponseDto (
        UUID idStatusHistory,
        String previousStatus,
        String newStatus,
        LocalDateTime updatedAt,
        String issueTitle,
        String userName
) {
    public static StatusHistoryResponseDto toStatusHistoryResponseDto(StatusHistory statusHistory) {
        return new StatusHistoryResponseDto(
                statusHistory.getIdStatusHistory(),
                statusHistory.getPreviousStatus().name(),
                statusHistory.getNewStatus().name(),
                statusHistory.getUpdatedAt(),
                statusHistory.getIssue().getTitle(),
                statusHistory.getUser().getUserName()
        );
    }
}