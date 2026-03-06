package SGI.repository;

import SGI.domain.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<ProjectMember, UUID> {}