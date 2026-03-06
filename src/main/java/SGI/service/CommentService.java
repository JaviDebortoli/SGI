package SGI.service;

import SGI.domain.Comment;
import SGI.domain.Issue;
import SGI.domain.User;
import SGI.dto.comment.CommentCreateDto;
import SGI.repository.CommentRepository;
import SGI.repository.IssueRepository;
import SGI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional (readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;

    @Transactional
    public void newComment(CommentCreateDto commentCreateDto) {
        Issue issue = issueRepository.findById(commentCreateDto.idIssue())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró la incidencia con ID = " + commentCreateDto.idIssue()
                ));

        User user = userRepository.findById(commentCreateDto.idUser())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el usuario con ID = " + commentCreateDto.idUser()
                ));

        Comment comment = new Comment();
        comment.setContent(commentCreateDto.content());
        comment.setIssue(issue);
        comment.setUser(user);
    }
}