package SGI.repository;

import SGI.domain.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID> {
    boolean existsByUserIdUserAndProjectIdProject(UUID userId, UUID projectId);
    List<ProjectMember> findAllByProjectIdProjectAndActiveTrue(UUID projectId);
    Optional<ProjectMember> findByUserIdUserAndProjectIdProjectAndActiveTrue(UUID userId, UUID projectId);
}