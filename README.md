# Sistema de Incidencias (SGI)

---

## Descripción

SGI (Sistema de Gestión de Incidencias) es un API backend que permite gestionar proyectos, incidencias, usuarios y roles dentro de un entorno colaborativo de desarrollo de software.

El objetivo del proyecto es demostrar conocimientos en desarrollo de APIs RESTful con Java Spring, aplicando buenas prácticas de arquitectura, modelado de dominio, persistencia con JPA y manejo de lógica de negocio en una aplicación backend.

El sistema permite:
- gestionar usuarios
- crear y administrar proyectos
- asignar miembros a proyectos con roles
- crear y gestionar incidencias
- asignar incidencias a usuarios
- manejar el estado de las incidencias mediante una máquina de estados
- registrar el historial de cambios de estado
- añadir comentarios a incidencias

--- 

## Tecnologías utilizadas
- Java 25
- Spring Boot
- Spring Data JPA
- Spring Web MVC
- Spring Security (Password Encoder)
- Hibernate
- PostgreSQL
- Lombok
- Jakarta Validation
- Maven

## Arquitectura
```
src/main/java
│
├── domain
│   ├── entidades JPA
│   └── enums de dominio
│
├── dto
│   ├── DTOs de entrada
│   └── DTOs de salida
│
├── repository
│   └── interfaces Spring Data JPA
│
├── service
│   └── lógica de negocio
│
└── controllers
    └── endpoints

```
Principios aplicados:
- Separación de responsabilidades
- DTO Pattern
- Domain Driven Design (enfoque simplificado)
- Encapsulación de lógica en entidades
- Validaciones en capa de entrada
- Uso de UUID como identificadores
- Soft delete
- Arquitectura en capas
- Transacciones controlada
- Uso de transacciones con @Transactional