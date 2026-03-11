package SGI.service;

import SGI.domain.Project;
import SGI.dto.project.ProjectCreateDto;
import SGI.dto.project.ProjectResponseDto;
import SGI.dto.project.ProjectUpdateDto;
import SGI.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Transactional
    public ProjectResponseDto createProject(ProjectCreateDto projectCreateDto) {
        // Crear el proyecto
        Project project = new Project();
        project.setProjectName(projectCreateDto.projectName());
        project.setProjectDescription(projectCreateDto.projectDescription());
        project.setActive(true);
        // Guardar el proyecto
        projectRepository.save(project);
        // Retornar el proyecto creado
        return ProjectResponseDto.toProjectResponseDto(project);
    }

    public List<ProjectResponseDto> getAllProjects() {
        // Retornar todos los proyectos activos
        return projectRepository.findAllProjectsByActiveTrue()
                .stream()
                .map(ProjectResponseDto::toProjectResponseDto)
                .toList();
    }

    public ProjectResponseDto getProjectById(UUID projectId) {
        // Retornar el proyecto activo con el ID indicado
        Project project = projectRepository.findProjectByIdProjectAndActiveTrue(projectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el proyecto con ID = " + projectId
                ));

        return ProjectResponseDto.toProjectResponseDto(project);
    }

    @Transactional
    public ProjectResponseDto updateProject(UUID projectId, ProjectUpdateDto projectUpdateDto) {
        // Encontrar el proyecto
        Project project = projectRepository.findProjectByIdProjectAndActiveTrue(projectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el proyecto con ID = " + projectId
                ));
        // Actualizar los datos
        project.setProjectName(projectUpdateDto.projectName());
        project.setProjectDescription(projectUpdateDto.projectDescription());
        // Retornar proyecto actualizado
        return ProjectResponseDto.toProjectResponseDto(project);
    }

    @Transactional
    public void deleteProject(UUID projectId) {
        // Encontrar el proyecto
        Project project = projectRepository.findProjectByIdProjectAndActiveTrue(projectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el proyecto con ID = " + projectId
                ));
        // Desactivar proyecto
        project.setActive(false);
    }
}