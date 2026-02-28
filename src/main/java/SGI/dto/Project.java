package SGI.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "projects", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_project", nullable = false)
    Long idProject;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String description;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    boolean active;

    @OneToMany(mappedBy = "issue")
    private List<Issue> Issues = new ArrayList<>();

    @OneToMany(mappedBy = "projectMember")
    private List<ProjectMember> ProjectMembers = new ArrayList<>();
}
