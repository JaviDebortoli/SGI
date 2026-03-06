package SGI.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id_role", nullable = false)
    private UUID idRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false)
    @Setter(AccessLevel.NONE)
    private RoleName roleName;

    @Column(name = "role_description", nullable = false)
    private String roleDescription;

    @OneToMany(mappedBy = "role")
    private List<ProjectMember> projectMembers = new ArrayList<>();
}