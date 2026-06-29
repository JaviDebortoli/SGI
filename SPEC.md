# SGI — Specification

Sistema de Gestión de Incidencias. Backend REST para la gestión de incidencias orientado a proyectos.

> **Cómo mantener este archivo**: cada vez que se modifique una entidad, un endpoint, una regla de negocio o una validación, actualizar la sección correspondiente. La estructura está organizada por recurso para que el cambio en el código tenga una sola sección de destino en esta spec.

> **Correspondencia con STORIES.md**: este archivo documenta el *cómo* técnico. Los requisitos de negocio que justifican cada regla y cada endpoint están en `STORIES.md`. Cuando se agregue o modifique una regla técnica, verificar que la historia de usuario y sus criterios de aceptación en `STORIES.md` reflejen el cambio, y viceversa.

---

## 1. Overview

SGI expone una API REST que permite administrar usuarios, proyectos, membresías, incidencias, comentarios e historial de cambios de estado. El alcance es similar al núcleo operativo de Jira o Trello, restringido al backend.

**Stack**: Java 25 · Spring Boot 4.0.3 · PostgreSQL · Spring Data JPA · Spring Security · Lombok · Bean Validation  
**Puerto**: `8080`  
**Autenticación**: HTTP Basic (credencial única, configurada por variables de entorno)

---

## 2. Domain Model

### Entidades

| Entidad | Tabla | PK | Soft delete |
|---|---|---|---|
| `User` | `users` | `id_user` UUID | `enabled = false` |
| `Project` | `projects` | `id_project` UUID | `active = false` |
| `ProjectMember` | `project_members` | `id_project_member` UUID | `active = false` |
| `Role` | `roles` | `id_role` UUID | — (catálogo) |
| `Issue` | `issues` | `id_issue` UUID | — |
| `Comment` | `comments` | `id_comment` UUID | — |
| `StatusHistory` | `status_histories` | `id_status_history` UUID | — |

### Relaciones

```
User ─────────────────────────── ProjectMember ──── Role
  │  reporter / assignee               │
  │                                    │
Issue ──────────────────────── Project
  │
  ├── Comment (User)
  └── StatusHistory (User)
```

- `ProjectMember` tiene unique constraint sobre `(id_project, id_user)`.
- `Issue.status` no tiene setter público — sólo se modifica a través de `Issue.changeStatus()`.
- `Role` es un catálogo gestionado a nivel de base de datos. No tiene endpoints de gestión.

### Enums

| Enum | Valores |
|---|---|
| `IssueStatus` | `OPEN` · `IN_PROGRESS` · `BLOCKED` · `DONE` |
| `IssuePriority` | `LOW` · `MEDIUM` · `HIGH` · `CRITICAL` |
| `IssueType` | `BUG` · `TASK` · `STORY` |
| `RoleName` | `ADMIN` · `MANAGER` · `DEVELOPER` |

---

## 3. Business Rules

- **Soft delete universal**: ninguna entidad se elimina físicamente. Los usuarios se desactivan con `enabled = false`; proyectos y membresías con `active = false`.
- **Auditing automático**: `createdAt` se asigna al persistir mediante `@CreatedDate` (JPA Auditing). No se puede enviar en el request.
- **Passwords**: se almacenan cifradas con BCrypt. Nunca se devuelven en ninguna respuesta.
- **Roles de proyecto**: son un catálogo en base de datos. Se referencian por `UUID` en los requests; el nombre del rol se devuelve como string en las respuestas.
- **Filtros activos**: las queries de listado y búsqueda por ID filtran siempre por `active = true` o `enabled = true`. Las entidades desactivadas no son visibles a través de la API.

---

## 4. State Machine — IssueStatus

El estado inicial de toda incidencia es `OPEN`. Las transiciones válidas están definidas en el enum `IssueStatus`; cualquier otra transición es rechazada con error.

```
        ┌─────────────────┐
        │      OPEN       │
        └────────┬────────┘
         ┌───────┴────────┐
         ▼                ▼
   ┌───────────┐    ┌─────────┐
   │ IN_PROGRESS│◄──►│ BLOCKED │
   └─────┬─────┘    └────┬────┘
         └──────┬─────────┘
                ▼
           ┌────────┐
           │  DONE  │  (terminal — no admite salidas)
           └────────┘
```

| Desde | Hacia |
|---|---|
| `OPEN` | `IN_PROGRESS`, `BLOCKED` |
| `IN_PROGRESS` | `BLOCKED`, `DONE` |
| `BLOCKED` | `IN_PROGRESS`, `DONE` |
| `DONE` | — |

Cada cambio de estado registra un `StatusHistory` con `previousStatus`, `newStatus`, el usuario que realizó el cambio y timestamp.

---

## 5. Resources

---

### 5.1 Users

#### Model

| Campo | Tipo | Notas |
|---|---|---|
| `idUser` | UUID | PK, generado |
| `userName` | String | Único en la tabla |
| `email` | String | Único en la tabla |
| `password` | String | BCrypt, nunca expuesto |
| `enabled` | boolean | `true` al crear |
| `createdAt` | LocalDateTime | Auto (JPA Auditing) |

#### Endpoints

| Método | Path | Body | Response | Status |
|---|---|---|---|---|
| `GET` | `/users` | — | `UserResponseDto[]` | 200 |
| `POST` | `/users` | `UserCreateDto` | `UserResponseDto` | 201 |
| `GET` | `/users/{userId}` | — | `UserResponseDto` | 200 |
| `PUT` | `/users/{userId}` | `UserUpdateDto` | `UserResponseDto` | 200 |
| `DELETE` | `/users/{userId}` | — | — | 200 |

#### Request schemas

**`UserCreateDto`**
```json
{
  "userName": "string (3–20 chars)",
  "email": "string (email válido)",
  "password": "string (4–30 chars, requiere mayúscula + minúscula + número + especial)"
}
```

**`UserUpdateDto`** — mismos campos que `UserCreateDto`, `userName` acepta hasta 100 chars.

#### Response schema — `UserResponseDto`
```json
{
  "idUser": "uuid",
  "userName": "string",
  "email": "string",
  "enabled": true,
  "createdAt": "datetime"
}
```

#### Business rules
- `userName` y `email` deben ser únicos. Al actualizar, la verificación excluye al propio usuario.
- `DELETE` es soft delete (`enabled = false`). El usuario desaparecerá de todos los listados.

---

### 5.2 Projects

#### Model

| Campo | Tipo | Notas |
|---|---|---|
| `idProject` | UUID | PK, generado |
| `projectName` | String | — |
| `projectDescription` | String | — |
| `createdAt` | LocalDateTime | Auto (JPA Auditing) |
| `active` | boolean | `true` al crear |

#### Endpoints

| Método | Path | Body | Response | Status |
|---|---|---|---|---|
| `GET` | `/projects` | — | `ProjectResponseDto[]` | 200 |
| `POST` | `/projects` | `ProjectCreateDto` | `ProjectResponseDto` | 201 |
| `GET` | `/projects/{projectId}` | — | `ProjectResponseDto` | 200 |
| `PUT` | `/projects/{projectId}` | `ProjectUpdateDto` | `ProjectResponseDto` | 200 |
| `DELETE` | `/projects/{projectId}` | — | — | 204 |

#### Request schemas

**`ProjectCreateDto` / `ProjectUpdateDto`**
```json
{
  "projectName": "string (3–30 chars)",
  "projectDescription": "string (3–200 chars)"
}
```

#### Response schema — `ProjectResponseDto`
```json
{
  "idProject": "uuid",
  "projectName": "string",
  "projectDescription": "string",
  "createdAt": "datetime",
  "active": true
}
```

#### Business rules
- Los listados y búsquedas retornan sólo proyectos activos.
- `DELETE` es soft delete (`active = false`).

---

### 5.3 Project Members

#### Model

| Campo | Tipo | Notas |
|---|---|---|
| `idProjectMember` | UUID | PK, generado |
| `project` | Project | FK |
| `user` | User | FK |
| `role` | Role | FK |
| `createdAt` | LocalDateTime | Auto (JPA Auditing) |
| `active` | boolean | `true` al asignar |

Unique constraint: `(id_project, id_user)`.

#### Endpoints

| Método | Path | Body | Response | Status |
|---|---|---|---|---|
| `GET` | `/projects/{projectId}/members` | — | `ProjectMemberResponseDto[]` | 200 |
| `POST` | `/projects/{projectId}/members` | `ProjectMemberCreateDto` | `ProjectMemberResponseDto` | 201 |
| `PUT` | `/projects/{projectId}/members/{userId}` | `ProjectMemberUpdateDto` | `ProjectMemberResponseDto` | 200 |
| `DELETE` | `/projects/{projectId}/members/{userId}` | — | — | 204 |

#### Request schemas

**`ProjectMemberCreateDto`**
```json
{
  "idProject": "uuid",
  "idUser": "uuid",
  "idRole": "uuid"
}
```

**`ProjectMemberUpdateDto`**
```json
{
  "idRole": "uuid"
}
```

#### Response schema — `ProjectMemberResponseDto`
```json
{
  "idProjectMember": "uuid",
  "projectName": "string",
  "userName": "string",
  "roleName": "ADMIN | MANAGER | DEVELOPER",
  "active": true
}
```

#### Business rules
- Un usuario no puede ser asignado dos veces al mismo proyecto.
- El proyecto debe estar activo y el usuario debe estar habilitado.
- `DELETE` es soft delete (`active = false`).
- El listado retorna sólo membresías activas.

---

### 5.4 Issues

#### Model

| Campo | Tipo | Notas |
|---|---|---|
| `idIssue` | UUID | PK, generado |
| `title` | String | — |
| `issueDescription` | String | — |
| `priority` | IssuePriority | `LOW / MEDIUM / HIGH / CRITICAL` |
| `status` | IssueStatus | Default `OPEN`; sólo modificable via `changeStatus()` |
| `type` | IssueType | `BUG / TASK / STORY` |
| `project` | Project | FK |
| `reporter` | User | FK — quién reportó el issue |
| `assignee` | User | FK — responsable actual |

#### Endpoints

| Método | Path | Body | Response | Status |
|---|---|---|---|---|
| `GET` | `/projects/{projectId}/issues` | — | `IssueResponseDto[]` | 200 |
| `POST` | `/projects/{projectId}/issues` | `IssueCreateDto` | `IssueResponseDto` | 201 |
| `GET` | `/issues/issue/{idIssue}` | — | `IssueResponseDto` | 200 |
| `GET` | `/issues/status/{status}` | — | `IssueResponseDto[]` | 200 |
| `GET` | `/issues/priority/{priority}` | — | `IssueResponseDto[]` | 200 |
| `PUT` | `/issues/{issueId}` | `IssueUpdateDto` | `IssueResponseDto` | 200 |
| `PATCH` | `/issues/{issueId}/assignee` | `IssueAssignmentDto` | `IssueResponseDto` | 200 |
| `PATCH` | `/users/{userId}/issues/{issueId}/status` | `IssueStatusUpdateDto` | `IssueResponseDto` | 200 |

#### Request schemas

**`IssueCreateDto`**
```json
{
  "title": "string (3–100 chars)",
  "issueDescription": "string (3–200 chars)",
  "priority": "LOW | MEDIUM | HIGH | CRITICAL",
  "type": "BUG | TASK | STORY",
  "idUserReporter": "uuid",
  "idUserAssignee": "uuid"
}
```

**`IssueUpdateDto`** — sólo modifica título, prioridad y tipo. No modifica estado ni asignado.
```json
{
  "title": "string (3–100 chars)",
  "priority": "LOW | MEDIUM | HIGH | CRITICAL",
  "type": "BUG | TASK | STORY"
}
```

**`IssueStatusUpdateDto`**
```json
{ "status": "IN_PROGRESS | BLOCKED | DONE" }
```

**`IssueAssignmentDto`**
```json
{ "assigneeId": "uuid" }
```

#### Response schema — `IssueResponseDto`
```json
{
  "idIssue": "uuid",
  "title": "string",
  "issueDescription": "string",
  "priority": "string",
  "status": "string",
  "type": "string",
  "projectName": "string",
  "userReporterName": "string",
  "userAssigneeName": "string"
}
```

#### Business rules
- El proyecto debe estar activo para crear o consultar issues.
- Reporter y assignee deben ser usuarios habilitados.
- `PUT /issues/{issueId}` no permite cambiar estado, proyecto, reporter ni assignee.
- Para cambiar el assignee, el nuevo usuario debe pertenecer al proyecto. No se puede reasignar al mismo usuario que ya está asignado.
- Los cambios de estado siguen las transiciones definidas en la máquina de estados (§4). Cada cambio genera un registro en `StatusHistory`.
- El estado inicial siempre es `OPEN` y no es configurable al crear.

---

### 5.5 Comments

#### Model

| Campo | Tipo | Notas |
|---|---|---|
| `idComment` | UUID | PK, generado |
| `content` | String | — |
| `createdAt` | LocalDateTime | Auto (JPA Auditing) |
| `issue` | Issue | FK |
| `user` | User | FK |

#### Endpoints

| Método | Path | Body | Response | Status |
|---|---|---|---|---|
| `GET` | `/issues/{issueId}/comments` | — | `CommentResponseDto[]` | 200 |
| `POST` | `/issues/{issueId}/comments` | `CommentCreateDto` | `CommentResponseDto` | 201 |

#### Request schema

**`CommentCreateDto`**
```json
{
  "content": "string (3–200 chars)",
  "idUser": "uuid"
}
```

#### Response schema — `CommentResponseDto`
```json
{
  "idComment": "uuid",
  "content": "string",
  "createdAt": "datetime",
  "issueTitle": "string",
  "userName": "string"
}
```

---

### 5.6 Status History

Registro inmutable de todos los cambios de estado de una incidencia. Se crea automáticamente al llamar a `PATCH /users/{userId}/issues/{issueId}/status`.

#### Model

| Campo | Tipo | Notas |
|---|---|---|
| `idStatusHistory` | UUID | PK, generado |
| `previousStatus` | IssueStatus | Estado anterior |
| `newStatus` | IssueStatus | Estado nuevo |
| `updatedAt` | LocalDateTime | Timestamp del cambio (JPA Auditing) |
| `issue` | Issue | FK (lazy) |
| `user` | User | FK — quién realizó el cambio (lazy) |

#### Endpoints

| Método | Path | Body | Response | Status |
|---|---|---|---|---|
| `GET` | `/issues/{issueId}/history` | — | `StatusHistoryResponseDto[]` | 200 |

El listado se ordena por `updatedAt` descendente (más reciente primero).

#### Response schema — `StatusHistoryResponseDto`
```json
{
  "idStatusHistory": "uuid",
  "previousStatus": "string",
  "newStatus": "string",
  "updatedAt": "datetime",
  "issueTitle": "string",
  "userName": "string"
}
```

---

## 6. Auth & Security

- **Mecanismo**: HTTP Basic. Todas las requests deben incluir el header `Authorization: Basic <base64(user:password)>`.
- **Credencial**: usuario único en memoria, configurado via `SGI_SECURITY_USERNAME` y `SGI_SECURITY_PASSWORD`.
- **CSRF**: deshabilitado (API REST stateless).
- **Excepción pública**: `/error` no requiere autenticación.
- **Sin autorización por roles de dominio**: la autenticación valida que el cliente sea quien dice ser, pero no hay control de acceso basado en el rol del `ProjectMember`. Cualquier usuario autenticado puede operar sobre cualquier recurso.

---

## 7. Error Handling

El sistema no tiene actualmente un manejador de excepciones global. Los errores de negocio se lanzan como `IllegalArgumentException` desde el service layer y resultan en un `500 Internal Server Error`. Los errores de validación de Bean Validation retornan `400 Bad Request`.

| Situación | Comportamiento actual |
|---|---|
| Entidad no encontrada | `IllegalArgumentException` → 500 |
| Transición de estado inválida | `IllegalArgumentException` → 500 |
| Violación de regla de negocio | `IllegalArgumentException` → 500 |
| Validación de DTO fallida | 400 (Spring MVC) |
| Sin autenticación | 401 (Spring Security) |
