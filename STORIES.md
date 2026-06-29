# SGI — User Stories

Historias de usuario del Sistema de Gestión de Incidencias. Describen los requisitos desde la perspectiva de negocio, sin referencias a la implementación técnica.

> **Correspondencia con SPEC.md**: cada historia y sus criterios de aceptación tienen contraparte técnica en `SPEC.md`. La sección de cada recurso en `SPEC.md` implementa las reglas de negocio aquí descritas. Cuando se agregue o modifique una historia, debe actualizarse la sección técnica correspondiente en `SPEC.md`, y viceversa.

---

## Actores

| Actor | Descripción |
|---|---|
| **Admin** | Administra la plataforma: gestiona usuarios y proyectos. |
| **Manager** | Gestiona un proyecto: asigna miembros, crea y reasigna incidencias. |
| **Developer** | Trabaja sobre las incidencias de sus proyectos: actualiza el estado y colabora con comentarios. |

> Un mismo usuario puede actuar como Admin, Manager o Developer según el contexto. Los roles son por proyecto.

---

## Epic 1 — Gestión de usuarios

### US-01 · Registrar un usuario

**Como** Admin,  
**quiero** registrar nuevos usuarios en la plataforma,  
**para** que puedan ser incorporados a proyectos y trabajar sobre incidencias.

**Criterios de aceptación**:
- El nombre de usuario debe ser único en la plataforma.
- El email debe ser único y tener formato válido.
- La contraseña debe cumplir un mínimo de complejidad: al menos una mayúscula, una minúscula, un número y un carácter especial.
- El usuario queda habilitado inmediatamente al ser registrado.

---

### US-02 · Consultar usuarios

**Como** Admin,  
**quiero** ver la lista de usuarios habilitados y consultar el detalle de uno en particular,  
**para** conocer quiénes están disponibles para asignar a proyectos.

**Criterios de aceptación**:
- El listado incluye sólo usuarios habilitados.
- Se puede consultar un usuario individual por su identificador.
- Las contraseñas no son visibles en ninguna consulta.

---

### US-03 · Actualizar un usuario

**Como** Admin,  
**quiero** actualizar el nombre de usuario, email o contraseña de un usuario existente,  
**para** mantener los datos de la plataforma actualizados.

**Criterios de aceptación**:
- Se puede actualizar el nombre de usuario, el email y la contraseña de forma conjunta.
- Si el nuevo nombre de usuario ya pertenece a otro usuario, la operación es rechazada.
- Si el nuevo email ya pertenece a otro usuario, la operación es rechazada.
- Un usuario puede conservar su nombre de usuario y email actuales sin que la operación falle.

---

### US-04 · Dar de baja un usuario

**Como** Admin,  
**quiero** deshabilitar un usuario que ya no debe tener acceso a la plataforma,  
**para** retirar su participación sin perder el historial de su actividad.

**Criterios de aceptación**:
- El usuario deshabilitado deja de aparecer en los listados.
- El historial de incidencias, comentarios y cambios de estado que realizó se conserva.

---

## Epic 2 — Gestión de proyectos

### US-05 · Crear un proyecto

**Como** Admin,  
**quiero** crear un nuevo proyecto con nombre y descripción,  
**para** que el equipo pueda organizarse y gestionar sus incidencias dentro de él.

**Criterios de aceptación**:
- Todo proyecto requiere un nombre y una descripción.
- El proyecto queda activo inmediatamente al ser creado.

---

### US-06 · Consultar proyectos

**Como** Admin o Manager,  
**quiero** ver la lista de proyectos activos y consultar el detalle de uno en particular,  
**para** tener visibilidad del estado general de la plataforma.

**Criterios de aceptación**:
- El listado incluye sólo proyectos activos.
- Se puede consultar un proyecto individual por su identificador.

---

### US-07 · Actualizar un proyecto

**Como** Admin,  
**quiero** modificar el nombre o la descripción de un proyecto existente,  
**para** corregir o refinar la información del proyecto.

**Criterios de aceptación**:
- Se puede actualizar el nombre y la descripción de forma conjunta.
- No se puede modificar un proyecto inactivo.

---

### US-08 · Archivar un proyecto

**Como** Admin,  
**quiero** archivar un proyecto que ya no está en curso,  
**para** retirarlo de la vista activa sin eliminar su historial.

**Criterios de aceptación**:
- El proyecto archivado deja de aparecer en los listados.
- Las incidencias y membresías del proyecto se conservan.

---

## Epic 3 — Membresías de proyecto

### US-09 · Agregar un miembro a un proyecto

**Como** Manager,  
**quiero** incorporar un usuario a mi proyecto asignándole un rol,  
**para** que pueda participar en la gestión de incidencias.

**Criterios de aceptación**:
- El usuario debe estar habilitado en la plataforma.
- El proyecto debe estar activo.
- Un usuario no puede ser incorporado dos veces al mismo proyecto.
- Al incorporarlo se debe especificar su rol: Admin, Manager o Developer.

---

### US-10 · Consultar miembros de un proyecto

**Como** Manager,  
**quiero** ver quiénes son los miembros activos de mi proyecto y qué rol tiene cada uno,  
**para** conocer la composición del equipo.

**Criterios de aceptación**:
- El listado incluye sólo membresías activas.
- Para cada miembro se muestra su nombre y su rol en el proyecto.

---

### US-11 · Cambiar el rol de un miembro

**Como** Manager,  
**quiero** cambiar el rol de un miembro dentro del proyecto,  
**para** reflejar cambios en las responsabilidades del equipo.

**Criterios de aceptación**:
- El miembro debe estar activo en el proyecto.
- El nuevo rol debe ser uno de los roles válidos del sistema.

---

### US-12 · Retirar un miembro de un proyecto

**Como** Manager,  
**quiero** retirar a un usuario de mi proyecto,  
**para** que deje de participar en él sin afectar el historial de su actividad.

**Criterios de aceptación**:
- La membresía queda inactiva; el usuario no aparece más en el listado del proyecto.
- Las incidencias asignadas al usuario dentro del proyecto no se modifican automáticamente.

---

## Epic 4 — Incidencias

### US-13 · Crear una incidencia

**Como** Manager o Developer,  
**quiero** registrar una nueva incidencia dentro de un proyecto,  
**para** que el equipo pueda hacerle seguimiento hasta su resolución.

**Criterios de aceptación**:
- Toda incidencia pertenece a un proyecto activo.
- Se debe especificar título, descripción, prioridad y tipo.
- Se debe indicar quién reporta la incidencia y quién es el responsable inicial.
- Tanto el reportero como el responsable deben ser usuarios habilitados.
- La incidencia se crea siempre en estado Abierta.

---

### US-14 · Consultar incidencias de un proyecto

**Como** Manager o Developer,  
**quiero** ver todas las incidencias de un proyecto,  
**para** tener una vista de conjunto del trabajo pendiente.

**Criterios de aceptación**:
- El listado muestra todas las incidencias del proyecto, independientemente de su estado.
- Para cada incidencia se muestra el responsable actual y el estado.

---

### US-15 · Filtrar incidencias por estado o prioridad

**Como** Manager o Developer,  
**quiero** filtrar las incidencias por estado o por prioridad,  
**para** enfocarme en lo más urgente o en lo que está bloqueado.

**Criterios de aceptación**:
- Se puede filtrar por cualquiera de los estados válidos: Abierta, En progreso, Bloqueada, Resuelta.
- Se puede filtrar por cualquiera de las prioridades: Baja, Media, Alta, Crítica.
- Los filtros operan sobre todas las incidencias del sistema, no sólo de un proyecto.

---

### US-16 · Actualizar una incidencia

**Como** Manager,  
**quiero** modificar el título, la prioridad o el tipo de una incidencia,  
**para** mantener su información actualizada a medida que evoluciona.

**Criterios de aceptación**:
- Se puede modificar el título, la prioridad y el tipo.
- No se puede modificar el estado, el proyecto, el reportero ni el responsable a través de esta acción.

---

### US-17 · Reasignar una incidencia

**Como** Manager,  
**quiero** cambiar el responsable de una incidencia,  
**para** redistribuir el trabajo dentro del equipo.

**Criterios de aceptación**:
- El nuevo responsable debe ser miembro activo del proyecto al que pertenece la incidencia.
- No se puede reasignar la incidencia al mismo usuario que ya está asignado.

---

### US-18 · Cambiar el estado de una incidencia

**Como** Developer,  
**quiero** actualizar el estado de una incidencia conforme avanzo en su resolución,  
**para** que el equipo tenga visibilidad del progreso real.

**Criterios de aceptación**:
- Sólo son válidas las siguientes transiciones:
  - Abierta → En progreso, Bloqueada
  - En progreso → Bloqueada, Resuelta
  - Bloqueada → En progreso, Resuelta
  - Resuelta no admite más cambios de estado.
- No se puede asignar el mismo estado que ya tiene la incidencia.
- Cada cambio de estado queda registrado con el nombre del usuario que lo realizó.

---

## Epic 5 — Colaboración

### US-19 · Comentar una incidencia

**Como** Developer o Manager,  
**quiero** agregar comentarios a una incidencia,  
**para** documentar decisiones, avances o impedimentos directamente en el contexto del trabajo.

**Criterios de aceptación**:
- Cualquier usuario habilitado puede comentar en cualquier incidencia.
- Un comentario requiere contenido y el usuario que lo escribe.
- Los comentarios no pueden editarse ni eliminarse una vez creados.

---

### US-20 · Consultar comentarios de una incidencia

**Como** Developer o Manager,  
**quiero** ver todos los comentarios de una incidencia,  
**para** entender el contexto y el historial de la conversación.

**Criterios de aceptación**:
- Se muestran todos los comentarios de la incidencia con el nombre del autor y la fecha.

---

### US-21 · Consultar el historial de estados de una incidencia

**Como** Manager,  
**quiero** ver el historial completo de cambios de estado de una incidencia,  
**para** auditar cómo evolucionó y quién realizó cada cambio.

**Criterios de aceptación**:
- El historial muestra cada transición con el estado anterior, el estado nuevo, el usuario responsable y la fecha del cambio.
- El historial se muestra ordenado del cambio más reciente al más antiguo.
- Sólo existe historial a partir del primer cambio de estado; la creación de la incidencia no genera una entrada.
