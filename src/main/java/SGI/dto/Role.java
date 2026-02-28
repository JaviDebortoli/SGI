package SGI.dto;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "project_member")
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
    private roleName roleName;
}