package SGI.controller;

import SGI.domain.IssuePriority;
import SGI.domain.IssueStatus;
import SGI.dto.issue.*;
import SGI.service.IssueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class IssueController {
    private final IssueService issueService;

    @GetMapping("/projects/{projectId}/issues")
    public ResponseEntity<List<IssueResponseDto>> getAllIssueForProject (@PathVariable UUID projectId) {
        List<IssueResponseDto> issues = issueService.getIssueByProject(projectId);

        return ResponseEntity.status(HttpStatus.OK).body(issues);
    }

    @PostMapping("/projects/{projectId}/issues")
    public ResponseEntity<IssueResponseDto> createIssueForProject (@PathVariable UUID projectId,
                                                                   @Valid @RequestBody IssueCreateDto issueCreateDto) {
        IssueResponseDto issue = issueService.createIssue(projectId, issueCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(issue);
    }

    @GetMapping("/issues/issue/{idIssue}")
    public ResponseEntity<IssueResponseDto> getIssueById (@PathVariable UUID idIssue) {
        IssueResponseDto issue = issueService.getIssueById(idIssue);

        return ResponseEntity.status(HttpStatus.OK).body(issue);
    }

    @GetMapping("/issues/status/{status}")
    public ResponseEntity<List<IssueResponseDto>> getIssuesByStatusB (@PathVariable IssueStatus status) {
        List<IssueResponseDto> issues = issueService.getIssueByStatus(status);

        return ResponseEntity.status(HttpStatus.OK).body(issues);
    }

    @GetMapping("/issues/priority/{priority}")
    public ResponseEntity<List<IssueResponseDto>> getIssuesByPriority (@PathVariable IssuePriority priority) {
        List<IssueResponseDto> issues = issueService.getIssueByPriority(priority);

        return ResponseEntity.status(HttpStatus.OK).body(issues);
    }

    @PutMapping("/issues/{issueId}")
    public ResponseEntity<IssueResponseDto> updateIssueById (@PathVariable UUID issueId,
                                                             @Valid @RequestBody IssueUpdateDto issueUpdateDto) {
        IssueResponseDto issue = issueService.updateIssue(issueId, issueUpdateDto);

        return ResponseEntity.status(HttpStatus.OK).body(issue);
    }

    @PatchMapping("/issues/{issueId}/assignee")
    public  ResponseEntity<IssueResponseDto> assignIssue (@PathVariable UUID issueId,
                                                          @Valid @RequestBody IssueAssignmentDto issueAssignmentDto) {
        IssueResponseDto issue = issueService.updateAssignee(issueId, issueAssignmentDto);

        return  ResponseEntity.status(HttpStatus.OK).body(issue);
    }

    @PatchMapping("/users/{userId}/issues/{id}/status")
    public  ResponseEntity<IssueResponseDto> changeIssueStatus (@PathVariable UUID userId,
                                                                @PathVariable UUID issueId,
                                                                @Valid @RequestBody IssueStatusUpdateDto issueStatusUpdateDto) {
        IssueResponseDto issue = issueService.updateStatus(userId, issueId, issueStatusUpdateDto);

        return  ResponseEntity.status(HttpStatus.OK).body(issue);
    }
}