package SGI.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import java.util.UUID;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "status_histories")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusHistory {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id_status_history", nullable = false)
    private UUID idStatusHistory;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status", nullable = false)
    @Setter(AccessLevel.NONE)
    private IssueStatus previousStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false)
    @Setter(AccessLevel.NONE)
    private IssueStatus newStatus;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "id_issue", nullable = false)
    private Issue issue;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
}
