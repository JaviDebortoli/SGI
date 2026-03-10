package SGI.service;

import SGI.dto.status_history.StatusHistoryResponseDto;
import SGI.repository.IssueRepository;
import SGI.repository.StatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatusHistoryService {
    private final StatusHistoryRepository statusHistoryRepository;
    private final IssueRepository issueRepository;

    public List<StatusHistoryResponseDto> getStatusHistoryForIssue(UUID issueId) {
        // Verificar existencia de la incidencia
        if (!issueRepository.existsByIdIssue(issueId)) {
            throw new IllegalArgumentException("No existe el issue con ID = " + issueId);
        }
        // Retornar el historial de estados
        return statusHistoryRepository.findByIssueIdIssueOrderByUpdatedAtDesc(issueId)
                .stream()
                .map(StatusHistoryResponseDto::toStatusHistoryResponseDto)
                .toList();
    }
}