# Sistema de Gestion de Incidencias (SGI)

SGI es un backend REST para la gestion de incidencias orientado a proyectos, con un alcance similar al núcleo operativo de herramientas como Jira o Trello. El sistema permite administrar usuarios, proyectos, membresías por proyecto, incidencias, comentarios e historial de cambios de estado.

## Estado actual del proyecto

El proyecto ya implementa un dominio funcional con persistencia JPA, validación de entradas, servicios transaccionales, autenticación HTTP Basic y una API REST organizada por recursos. El enfoque actual esta centrado en el backend y no incluye frontend.

## Capacidades implementadas

### Usuarios
- Alta de usuarios.
- Consulta de todos los usuarios habilitados.
- Consulta de usuario por `UUID`.
- Actualización de username, email y password.
- Baja lógica mediante `enabled = false`.
- Cifrado de password con `BCryptPasswordEncoder`.
- Validación de unicidad de username y email.

### Proyectos
- Alta de proyectos.
- Consulta de todos los proyectos activos.
- Consulta de proyecto por `UUID`.
- Actualización de nombre y descripción.
- Baja lógica mediante `active = false`.

### Miembros de proyecto
- Asignación de usuarios a proyectos con un rol.
- Consulta de miembros activos por proyecto.
- Cambio de rol dentro del proyecto.
- Baja lógica de membresía.
- Restricción de unicidad por combinación proyecto/usuario.

### Incidencias
- Creación de incidencias dentro de un proyecto.
- Consulta de incidencias por proyecto.
- Consulta de incidencia por `UUID`.
- Filtrado por estado.
- Filtrado por prioridad.
- Actualización de título, prioridad y tipo.
- Reasignación de responsable.
- Asociación explicita de reporter y assignee.

### Flujo de estados
- Estado inicial `OPEN`.
- Transiciones validadas en el dominio:
  - `OPEN -> IN_PROGRESS`, `BLOCKED`
  - `IN_PROGRESS -> BLOCKED`, `DONE`
  - `BLOCKED -> IN_PROGRESS`, `DONE`
  - `DONE` no admite transiciones salientes
- Rechazo de cambios nulos, repetidos o invalidos.
- Registro del historial de cambios con usuario responsable.

### Comentarios e historial
- Alta de comentarios por incidencia.
- Consulta de comentarios de una incidencia.
- Consulta del historial de estados de una incidencia.
- Registro automático de timestamps de creación y actualización mediante JPA Auditing.

## Modelo de dominio

Entidades principales:
- `User`
- `Project`
- `ProjectMember`
- `Role`
- `Issue`
- `Comment`
- `StatusHistory`

Enums de dominio:
- `IssuePriority`: `LOW`, `MEDIUM`, `HIGH`, `CRITICAL`
- `IssueStatus`: `OPEN`, `IN_PROGRESS`, `BLOCKED`, `DONE`
- `IssueType`: `BUG`, `TASK`, `STORY`
- `RoleName`: `ADMIN`, `MANAGER`, `DEVELOPER`

Relaciones relevantes:
- Un `Project` tiene muchas `Issue` y muchas `ProjectMember`.
- Un `ProjectMember` vincula `User` + `Project` + `Role`.
- Una `Issue` pertenece a un `Project`, tiene `reporter`, `assignee`, `comments` e `statusHistories`.
- Un `Comment` pertenece a una `Issue` y a un `User`.
- Un `StatusHistory` pertenece a una `Issue` y a un `User`.

## Endpoints expuestos

### Usuarios
- `GET /users`
- `POST /users`
- `GET /users/{userId}`
- `PUT /users/{userId}`
- `DELETE /users/{userId}`

### Proyectos
- `GET /projects`
- `POST /projects`
- `GET /projects/{projectId}`
- `PUT /projects/{projectId}`
- `DELETE /projects/{projectId}`

### Miembros de proyecto
- `GET /projects/{projectId}/members`
- `POST /projects/{projectId}/members`
- `PUT /projects/{projectId}/members/{userId}`
- `DELETE /projects/{projectId}/members/{userId}`

### Incidencias
- `GET /projects/{projectId}/issues`
- `POST /projects/{projectId}/issues`
- `GET /issues/issue/{idIssue}`
- `GET /issues/status/{status}`
- `GET /issues/priority/{priority}`
- `PUT /issues/{issueId}`
- `PATCH /issues/{issueId}/assignee`
- `PATCH /users/{userId}/issues/{id}/status`

### Comentarios e historial
- `GET /issues/{issueId}/comments`
- `POST /issues/{issueId}/comments`
- `GET /issues/{issueId}/history`

## Tecnologías utilizadas

- Java 25
- Spring Boot 4.0.3
- Spring Web MVC
- Spring Data JPA
- Spring Security
- Hibernate / Jakarta Persistence
- Jakarta Validation
- PostgreSQL
- Lombok
- Maven Wrapper

## Patrones y decisiones de diseño

- Arquitectura en capas: `controller`, `service`, `repository`, `domain`, `dto`.
- DTO Pattern: separación clara entre payloads de entrada y salida.
- Rich Domain Model: la lógica de transición de estados vive en `Issue`.
- Repository Pattern con Spring Data JPA.
- Service Layer para la lógica de negocio y coordinación transaccional.
- Soft delete en usuarios, proyectos y membresías.
- Identificadores `UUID` en todas las entidades principales.
- Validación declarativa con anotaciones Bean Validation.
- Convenciones REST para modelar recursos y subrecursos.
- Auditoria automática con `@EnableJpaAuditing`, `@CreatedDate` y `@LastModifiedDate`.

## Estructura del proyecto

```text
src/main/java/SGI
|-- config        Configuracion de seguridad
|-- controller    Endpoints REST
|-- domain        Entidades JPA y enums
|-- dto           DTOs de entrada y salida
|-- repository    Repositorios Spring Data JPA
|-- service       Logica de negocio
`-- SgiApplication.java
```

## Persistencia y base de datos

- Base de datos objetivo: PostgreSQL.
- La aplicación usa variables de entorno para la conexión:
  - `DB_SGI_URL`
  - `DB_USER_NAME`
  - `DB_PASSWORD`
- La autenticación de Spring Security también se configura por variables de entorno:
  - `SGI_SECURITY_USERNAME`
  - `SGI_SECURITY_PASSWORD`
- `spring.jpa.hibernate.ddl-auto=validate`: Hibernate valida el esquema existente, pero no lo crea ni lo actualiza.
- `spring.jpa.open-in-view=false`: la sesión JPA no permanece abierta durante la serialización HTTP.

## Seguridad

La aplicación incluye Spring Security y un `PasswordEncoder` BCrypt.

Configuración actual:
- Se define un usuario en memoria con credenciales leídas desde propiedades:
  - `sgi.security.username=${SGI_SECURITY_USERNAME}`
  - `sgi.security.password=${SGI_SECURITY_PASSWORD}`
  - rol: `USER`
- `httpBasic()` esta habilitado.
- CSRF está desactivado.
- `/error` esta permitido sin autenticación.
- Todas las demás solicitudes requieren autenticación con `anyRequest().authenticated()`.

En otras palabras: la API ya no expone sus endpoints públicamente por defecto. Cualquier acceso a recursos de negocio requiere autenticarse, aunque todavia no existe autorizacion por roles o permisos de dominio.

## Validaciones y reglas de negocio

- Passwords con complejidad minima: mayúscula, minúscula, número y caracter especial.
- Restricciones de longitud en nombres, descripciones, títulos y comentarios.
- No se permite crear usuarios con username o email repetidos.
- No se permite asignar dos veces el mismo usuario al mismo proyecto.
- No se permite reasignar una incidencia al mismo usuario ya asignado.
- Para reasignar una incidencia, el nuevo responsable debe pertenecer al proyecto.
- Cada cambio de estado registra `previousStatus`, `newStatus`, usuario y timestamp.

## Respuestas de la API

El proyecto devuelve DTOs de salida que exponen información pensada para consumo cliente, por ejemplo:
- nombres de proyecto en vez de solo IDs en incidencias
- nombre del usuario reportero y asignado
- nombre del rol en membresías
- timestamps de creación y actualización donde corresponde

## Como ejecutar el proyecto

### Requisitos
- JDK 25
- PostgreSQL con el esquema ya creado y compatible con las entidades
- Maven (o usar el wrapper incluido)

### Variables de entorno

```powershell
$env:DB_SGI_URL="jdbc:postgresql://localhost:5432/sgi"
$env:DB_USER_NAME="postgres"
$env:DB_PASSWORD="tu_password"
$env:SGI_SECURITY_USERNAME="user"
$env:SGI_SECURITY_PASSWORD="1234"
```

### Ejecución

```powershell
.\mvnw spring-boot:run
```

La aplicación se ejecuta por defecto en `http://localhost:8080`.

## Autor

Proyecto desarrollado por Javier M. Debórtoli como práctica de aprendizaje en Spring y desarrollo backend en Java.
