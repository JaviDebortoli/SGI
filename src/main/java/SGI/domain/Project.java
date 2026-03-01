package SGI.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import java.util.UUID;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "projects")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id_project", nullable = false)
    private UUID idProject;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    boolean active;

    @OneToMany(mappedBy = "project")
    private List<Issue> Issues = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<ProjectMember> ProjectMembers = new ArrayList<>();
}
