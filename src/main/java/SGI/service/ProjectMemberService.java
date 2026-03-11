package SGI.service;

import SGI.domain.Project;
import SGI.domain.ProjectMember;
import SGI.domain.Role;
import SGI.domain.User;
import SGI.dto.project_member.ProjectMemberCreateDto;
import SGI.dto.project_member.ProjectMemberResponseDto;
import SGI.dto.project_member.ProjectMemberUpdateDto;
import SGI.repository.ProjectMemberRepository;
import SGI.repository.ProjectRepository;
import SGI.repository.RoleRepository;
import SGI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public ProjectMemberResponseDto assignUser (UUID projectId, ProjectMemberCreateDto projectMemberCreateDto) {
        // Encontrar proyecto
        Project project = projectRepository.findProjectByIdProjectAndActiveTrue(projectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el proyecto con ID = " + projectId
                ));
        // Encontrar Usuario
        User user = userRepository.findByIdUserAndEnabledTrue(projectMemberCreateDto.idUser())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el usuario con ID = " + projectMemberCreateDto.idUser()
                ));
        // Encontrar Rol
        Role role = roleRepository.findById(projectMemberCreateDto.idRole())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el rol con ID = " + projectMemberCreateDto.idRole()
                ));
        // Verificar asignación
        if (projectMemberRepository.existsByUserIdUserAndProjectIdProject(projectMemberCreateDto.idUser(), projectId)) {
            throw new IllegalArgumentException("El usuario ya pertenece a este proyecto");
        }
        // Crear asignación al proyecto
        ProjectMember projectMember = new ProjectMember();
        projectMember.setProject(project);
        projectMember.setUser(user);
        projectMember.setRole(role);
        projectMember.setActive(true);
        // Guardar asignación
        projectMemberRepository.save(projectMember);
        // Retornar asignacion
        return ProjectMemberResponseDto.toProjectMemberResponseDto(projectMember);
    }

    public List<ProjectMemberResponseDto> getAllProjectMembersFromProject(UUID projectId) {
        // Verificar si existe el proyecto
        if(projectRepository.existsByIdProjectAndActiveTrue(projectId)) {
            throw new IllegalArgumentException("No se encontró el proyecto con ID = " + projectId);
        }
        // Retornar todas las asignaciones a un proyecto
        return projectMemberRepository.findAllByProjectIdProjectAndActiveTrue(projectId)
                .stream()
                .map(ProjectMemberResponseDto::toProjectMemberResponseDto)
                .toList();
    }

    @Transactional
    public ProjectMemberResponseDto updateRole (UUID projectId, UUID userId, ProjectMemberUpdateDto projectMemberUpdateDto) {
        // Verificar si existe el proyecto
        if(projectRepository.existsByIdProjectAndActiveTrue(projectId)) {
            throw new IllegalArgumentException("No se encontró el proyecto con ID = " + projectId);
        }
        // Verificar si existe el usuario
        if(!userRepository.existsByIdUserAndEnabledTrue(userId)) {
            throw new IllegalArgumentException("No se encontró el usuario con ID = " + userId);
        }
        // Encontrar la asignación
        ProjectMember projectMember = projectMemberRepository.findByUserIdUserAndProjectIdProjectAndActiveTrue(userId, projectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El usuario ID = " + userId + " no pertenece al proyecto ID = " + projectId
                ));
        // Encontrar rol
        Role role = roleRepository.findById(projectMemberUpdateDto.idRole())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe el rol con ID = " + projectMemberUpdateDto.idRole()
        ));
        // Actualizar el rol
        projectMember.setRole(role);
        // Retornar asignación actualizada
        return ProjectMemberResponseDto.toProjectMemberResponseDto(projectMember);
    }

    @Transactional
    public void deleteAssignedUser (UUID projectId, UUID userId) {
        // Verificar si existe el proyecto
        if(projectRepository.existsByIdProjectAndActiveTrue(projectId)) {
            throw new IllegalArgumentException("No se encontró el proyecto con ID = " + projectId);
        }
        // Verificar si existe el usuario
        if(!userRepository.existsByIdUserAndEnabledTrue(userId)) {
            throw new IllegalArgumentException("No se encontró el usuario con ID = " + userId);
        }
        // Encontrar la asignación
        ProjectMember projectMember = projectMemberRepository.findByUserIdUserAndProjectIdProjectAndActiveTrue(userId, projectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El usuario ID = " + userId + " no pertenece al proyecto ID = " + projectId
                ));
        // Desactivar asignación
        projectMember.setActive(false);
    }
}