package SGI.service;

import SGI.domain.Comment;
import SGI.domain.Issue;
import SGI.domain.User;
import SGI.dto.comment.CommentCreateDto;
import SGI.dto.comment.CommentResponseDto;
import SGI.repository.CommentRepository;
import SGI.repository.IssueRepository;
import SGI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional (readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createComment (UUID issueId, CommentCreateDto commentCreateDto) {
        // Obtener el Issue
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró la incidencia con ID = " + issueId
                ));
        // Obtener el Usuario
        User user = userRepository.findById(commentCreateDto.idUser())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el usuario con ID = " + commentCreateDto.idUser()
                ));
        // Crear el Comment
        Comment comment = new Comment();
        comment.setContent(commentCreateDto.content());
        comment.setIssue(issue);
        comment.setUser(user);
        // Guardar el Comment
        commentRepository.save(comment);
    }

    public List<CommentResponseDto> getCommentsForIssue(UUID issueId) {
        // Retornar todos los Comments de un Issue
        return commentRepository.findByIssueId(issueId)
                .stream()
                .map(CommentResponseDto::toCommentResponseDto)
                .toList();
    }
}