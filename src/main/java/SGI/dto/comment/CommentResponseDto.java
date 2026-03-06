package SGI.dto.comment;

import SGI.domain.Comment;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponseDto (
        UUID idComment,
        String content,
        LocalDateTime createdAt,
        String issueTitle,
        String userName
) {
    public static CommentResponseDto toCommentResponseDto(Comment comment) {
        return new CommentResponseDto(
                comment.getIdComment(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getIssue().getTitle(),
                comment.getUser().getUserName()
        );
    }
}