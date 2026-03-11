package SGI.controller;

import SGI.dto.project_member.ProjectMemberCreateDto;
import SGI.dto.project_member.ProjectMemberResponseDto;
import SGI.dto.project_member.ProjectMemberUpdateDto;
import SGI.service.ProjectMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectMemberController {
    private final ProjectMemberService projectMemberService;

    @GetMapping("/{projectId}/members")
    public ResponseEntity<List<ProjectMemberResponseDto>> getAllProjectMembers (@PathVariable UUID projectId) {
        List<ProjectMemberResponseDto> projectMembers = projectMemberService.getAllProjectMembersFromProject(projectId);

        return ResponseEntity.status(HttpStatus.OK).body(projectMembers);
    }

    @PostMapping("/{projectId}/members")
    public ResponseEntity<ProjectMemberResponseDto> createProjectMember (@PathVariable UUID projectId,
                                                                         @Valid @RequestBody ProjectMemberCreateDto projectMemberCreateDto) {
        ProjectMemberResponseDto projectMember = projectMemberService.assignUser(projectId, projectMemberCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(projectMember);
    }

    @PutMapping("/{projectId}/members/{userId}")
    public ResponseEntity<ProjectMemberResponseDto> updateProjectMember (@PathVariable UUID projectId,
                                                                         @PathVariable UUID userId,
                                                                         @Valid @RequestBody ProjectMemberUpdateDto projectMemberUpdateDto) {
        ProjectMemberResponseDto projectMember = projectMemberService.updateRole(projectId, userId, projectMemberUpdateDto);

        return ResponseEntity.status(HttpStatus.OK).body(projectMember);
    }

    @DeleteMapping("/{projectId}/members/{userId}")
    public ResponseEntity<Void> deleteProjectMember (@PathVariable UUID projectId, @PathVariable UUID userId) {
        projectMemberService.deleteAssignedUser(projectId, userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}