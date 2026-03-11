package SGI.controller;

import SGI.dto.project.ProjectCreateDto;
import SGI.dto.project.ProjectResponseDto;
import SGI.dto.project.ProjectUpdateDto;
import SGI.service.ProjectService;
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
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getAllProjects() {
        List<ProjectResponseDto> projects = projectService.getAllProjects();

        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject (@Valid @RequestBody ProjectCreateDto projectCreateDto) {
        ProjectResponseDto project = projectService.createProject(projectCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDto> getProjectById (@PathVariable UUID projectId) {
        ProjectResponseDto project = projectService.getProjectById(projectId);

        return ResponseEntity.status(HttpStatus.OK).body(project);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDto> updateProjectById (@PathVariable UUID projectId,
                                                            @Valid @RequestBody ProjectUpdateDto projectUpdateDto) {
        ProjectResponseDto project = projectService.updateProject(projectId, projectUpdateDto);

        return ResponseEntity.status(HttpStatus.OK).body(project);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDto> deleteProjectById (@PathVariable UUID projectId) {
        projectService.deleteProject(projectId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}