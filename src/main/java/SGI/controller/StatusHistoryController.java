package SGI.controller;

import SGI.dto.status_history.StatusHistoryResponseDto;
import SGI.service.StatusHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/issues")
@RequiredArgsConstructor
public class StatusHistoryController {
    private final StatusHistoryService statusHistoryService;

    @GetMapping("/{issueId}/history")
    public ResponseEntity<List<StatusHistoryResponseDto>> getStatusHistoryForIssue(@PathVariable UUID issueId) {
        List<StatusHistoryResponseDto> statusHistory = statusHistoryService.getStatusHistoryForIssue(issueId);

        return ResponseEntity.status(HttpStatus.OK).body(statusHistory);
    }
}