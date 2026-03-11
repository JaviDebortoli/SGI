package SGI.service;

import SGI.domain.*;
import SGI.dto.issue.*;
import SGI.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional (readOnly = true)
public class IssueService {
    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final StatusHistoryRepository statusHistoryRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Transactional
    public IssueResponseDto createIssue(UUID idProject, IssueCreateDto issueCreateDto) {
        // Encontrar proyecto
        Project project = projectRepository.findProjectByIdProjectAndActiveTrue(idProject)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el proyecto con ID = " + idProject
                ));
        // Encontrar Usuario que reporta
        User reporter = userRepository.findByIdUserAndEnabledTrue(issueCreateDto.idUserReporter())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el usuario con ID = " + issueCreateDto.idUserReporter()
                ));
        // Encontrar Usuario asignado
        User assignee = userRepository.findByIdUserAndEnabledTrue(issueCreateDto.idUserAssignee())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el usuario con ID = " + issueCreateDto.idUserAssignee()
                ));
        // Crear incidencia
        Issue issue = new Issue();
        issue.setTitle(issueCreateDto.title());
        issue.setIssueDescription(issueCreateDto.issueDescription());
        issue.setPriority(issueCreateDto.priority());
        issue.setType(issueCreateDto.type());
        issue.setProject(project);
        issue.setReporter(reporter);
        issue.setAssignee(assignee);
        // Guardar incidencia
        issueRepository.save(issue);
        // Retornar incidencia creada
        return IssueResponseDto.toIssueResponseDto(issue);
    }

    public IssueResponseDto getIssueById (UUID idIssue) {
        // Encontrar la incidencia
        Issue issue = issueRepository.findByIdIssue(idIssue)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró la incidencia con ID = " + idIssue
                ));
        // Verificar que el proyecto asociado este activo
        if (!projectRepository.existsByIdProjectAndActiveTrue(issue.getProject().getIdProject())) {
            throw new IllegalArgumentException("El projeto no existe");
        }
        // Retornar incidencia
        return IssueResponseDto.toIssueResponseDto(issue);
    }

    public List<IssueResponseDto> getIssueByProject(UUID idProject) {
        // Verificar si existe el proyecto
        if(projectRepository.existsByIdProjectAndActiveTrue(idProject)) {
            throw new IllegalArgumentException("No se encontró el proyecto con ID = " + idProject);
        }
        // Retornar incidencias del proyecto
        return issueRepository.findByProjectIdProject(idProject)
                .stream()
                .map(IssueResponseDto::toIssueResponseDto)
                .toList();
    }

    public List<IssueResponseDto> getIssueByStatus(IssueStatus status) {
        // Retornar incidencias con el estado indicado
        return issueRepository.findByStatus(status)
                .stream()
                .map(IssueResponseDto::toIssueResponseDto)
                .toList();
    }

    public List<IssueResponseDto> getIssueByPriority(IssuePriority priority) {
        // Retornar incidencias con la prioridad indicada
        return issueRepository.findByPriority(priority)
                .stream()
                .map(IssueResponseDto::toIssueResponseDto)
                .toList();
    }

    @Transactional
    public IssueResponseDto updateIssue(UUID idIssue, IssueUpdateDto issueUpdateDto) {
        // Encontrar la incidencia
        Issue issue = issueRepository.findByIdIssue(idIssue)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró la incidencia con ID = " + idIssue
                ));
        // Actualizar incidencia
        issue.setTitle(issueUpdateDto.title());
        issue.setPriority(issueUpdateDto.priority());
        issue.setType(issueUpdateDto.type());
        // Retornar incidencia actualizada
        return IssueResponseDto.toIssueResponseDto(issue);
    }

    @Transactional
    public IssueResponseDto updateStatus(UUID idIssue, UUID idUser, IssueStatusUpdateDto issueStatusUpdateDto) {
        // Encontrar la incidencia
        Issue issue = issueRepository.findByIdIssue(idIssue)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró la incidencia con ID = " + idIssue
                ));
        // Encontrar Usuario que cambia el estado de la incidencia
        User user = userRepository.findByIdUserAndEnabledTrue(idUser)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el usuario con ID = " + idUser
                ));
        // Actualizar historial de estados
        StatusHistory history = new StatusHistory();
        history.setIssue(issue);
        history.setPreviousStatus(issue.getStatus());
        history.setNewStatus(issueStatusUpdateDto.status());
        history.setUser(user);
        statusHistoryRepository.save(history);
        // Actualizar estado de la incidencia
        issue.changeStatus(issueStatusUpdateDto.status());
        // Retornar incidencia con estado actualizado
        return IssueResponseDto.toIssueResponseDto(issue);
    }

    @Transactional
    public IssueResponseDto updateAssignee(UUID idIssue, IssueAssignmentDto issueAssignmentDto) {
        // Encontrar usuario
        User assignee = userRepository.findByIdUserAndEnabledTrue(issueAssignmentDto.assigneeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el usuario con ID = " + issueAssignmentDto.assigneeId()
                ));
        // Encontrar la incidencia
        Issue issue = issueRepository.findByIdIssue(idIssue)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró la incidencia con ID = " + idIssue
                ));
        // Verificar que el usuario sea miembro del proyecto de la incidencia
        if (!projectMemberRepository.existsByUserIdUserAndProjectIdProject(assignee.getIdUser(), issue.getProject().getIdProject())) {
            throw new IllegalArgumentException("El usuario no pertenece al proyecto");
        }
        // Verificar si el usuario ya esta asignado a esta incidencia
        if (issueAssignmentDto.assigneeId().equals(issue.getAssignee().getIdUser())) {
            throw new IllegalArgumentException("El usuario ya fue asignado a esta incidencia");
        }
        // Actualizar incidencia
        issue.setAssignee(assignee);
        // Retornar incidencia asignada/reasignada
        return IssueResponseDto.toIssueResponseDto(issue);
    }
}