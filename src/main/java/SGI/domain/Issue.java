package SGI.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "issues")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Issue {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id_issue", nullable = false)
    private UUID idIssue;

    @Column(nullable = false)
    private String title;

    @Column(name = "issue_description", nullable = false)
    private String issueDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssuePriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private IssueStatus status = IssueStatus.OPEN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssueType type;

    @ManyToOne
    @JoinColumn(name = "id_project", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "id_user_reporter", nullable = false)
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "id_user_assignee", nullable = false)
    private User assignee;

    @OneToMany(mappedBy = "issue")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "issue")
    private List<StatusHistory> statusHistories = new ArrayList<>();

    public void changeStatus(IssueStatus newStatus) {
        // Validar que no sea null
        if (newStatus == null) {
            throw new IllegalArgumentException("Estado inválido");
        }
        // Validar que no sea el estado actual
        if (this.status == newStatus) {
            throw new IllegalArgumentException("El issue ya tiene ese estado");
        }
        // Validar la transicion
        if (!this.status.canTransitionTo(newStatus)) {
            throw new IllegalArgumentException("Transicion inválida de " + status + " a " + newStatus);
        }
        // Cambiar estado
        this.status = newStatus;
    }
}