# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```powershell
# Set required env vars before running
$env:DB_SGI_URL="jdbc:postgresql://localhost:5432/sgi"
$env:DB_USER_NAME="postgres"
$env:DB_PASSWORD="tu_password"
$env:SGI_SECURITY_USERNAME="user"
$env:SGI_SECURITY_PASSWORD="1234"

# Run the application
.\mvnw spring-boot:run

# Build (skip tests)
.\mvnw clean package -DskipTests

# Run all tests
.\mvnw test

# Run a single test class
.\mvnw test -Dtest=SgiApplicationTests
```

The app starts on `http://localhost:8080`. All endpoints require HTTP Basic auth using the credentials from `SGI_SECURITY_USERNAME` / `SGI_SECURITY_PASSWORD`.

## Architecture

Classic layered: `Controller → Service → Repository`. Packages: `controller`, `service`, `repository`, `domain`, `dto`, `config`.

**DTO pattern**: All DTOs are Java records. Response DTOs hold a static factory method (`toXResponseDto(entity)`) that does the mapping inline — no MapStruct. Input DTOs carry Bean Validation annotations.

**Transactions**: Every service class is `@Transactional(readOnly = true)` at the class level. Write methods override with `@Transactional`.

**Soft deletes**: `User.enabled`, `Project.active`, `ProjectMember.active`. Repository queries filter by these flags (e.g., `findByIdUserAndEnabledTrue`). No hard deletes anywhere.

**State machine**: `IssueStatus` is a rich enum — each constant overrides `allowedTransitions()` to declare valid next states. `Issue.changeStatus()` delegates to the enum and throws `IllegalArgumentException` on invalid transitions. The state machine lives in the domain, not the service.

**Audit timestamps**: `@EnableJpaAuditing` + `@EntityListeners(AuditingEntityListener.class)` + `@CreatedDate` / `@LastModifiedDate`. Entities opt in individually. `StatusHistory` uses `@LastModifiedDate` for its timestamp (not `@CreatedDate`).

**Security**: Single in-memory user via `SecurityConfig`. Application `User` entities are separate from Spring Security — there is no per-user authentication yet; the API is protected by a single shared credential.

## Fixed bugs

All three bugs identified on initial exploration have been resolved:

1. **`IssueController` — path variable mismatch + swapped service args** ✓
   Renamed `{id}` to `{issueId}` in the path and corrected the argument order in the `updateStatus` call.

2. **Inverted `existsBy*` guards** ✓
   Added `!` to the five inverted conditions across `IssueService.getIssueByProject` and three methods in `ProjectMemberService`.

3. **`UserService.updateUser` — self-uniqueness check** ✓
   Replaced `existsByUserName` / `existsByEmail` with `existsByUserNameAndIdUserNot` / `existsByEmailAndIdUserNot` (new methods added to `UserRepository`).
