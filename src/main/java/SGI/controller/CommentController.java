package SGI.controller;

import SGI.dto.comment.CommentCreateDto;
import SGI.dto.comment.CommentResponseDto;
import SGI.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/issues")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{issueId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getUserById (@PathVariable UUID issueId) {
        List<CommentResponseDto> comments = commentService.getCommentsForIssue(issueId);

        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    @PostMapping("/{issueId}/comments")
    public ResponseEntity<CommentResponseDto> getUserById (@PathVariable UUID issueId,
                                                           @Valid @RequestBody CommentCreateDto commentCreateDto) {
        CommentResponseDto comment = commentService.createComment(issueId, commentCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }
}