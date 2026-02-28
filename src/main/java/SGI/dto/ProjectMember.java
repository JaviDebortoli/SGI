package SGI.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "project_member", uniqueConstraints = {
        @UniqueConstraint(columnNames = "{id_project, id_user}")
})
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMember {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id_project_member", nullable = false)
    private UUID idProjectMember;

    @ManyToOne
    @JoinColumn(name = "id_project", nullable = false)
    private Project idProject;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User idUser;

    @ManyToOne
    @JoinColumn(name = "id_role", nullable = false)
    private Role idRole;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;
}