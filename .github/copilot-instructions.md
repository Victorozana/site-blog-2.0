# Copilot instructions for site-blog-2.0

Purpose: Help Copilot (and future Copilot sessions) quickly understand how to build, test, run, and navigate this repository.

---

## Quick build / run / test commands

- Dev (live coding + Dev UI):  (Unix) `./mvnw quarkus:dev`  (Windows PowerShell/CMD) `.\mvnw.cmd quarkus:dev`
- Package (jar): `./mvnw package`
- Run packaged app: `java -jar target/quarkus-app/quarkus-run.jar`
- Build uber-jar: `./mvnw package -Dquarkus.package.jar.type=uber-jar`
- Native build: `./mvnw package -Dnative`  (or `-Dquarkus.native.container-build=true` to build in a container)
- Run tests (all): `./mvnw test`
- Run a single unit test: `./mvnw -Dtest=ClassNameTest test`
- Run a single test method: `./mvnw -Dtest=ClassNameTest#methodName test`

Notes: Tests use Maven Surefire. Integration tests (if added) will run with Failsafe per pom configuration.

---

## High-level architecture (big picture)

- Framework: Quarkus (Java 17). The project uses Qute templates for server-side HTML and Quarkus Web Bundler for front-end assets.
- Layers:
  - Controller layer (src/main/java/com/blog/controller): exposes HTML pages (GET returning TemplateInstance) and JSON endpoints (usually under `/api` or `/data`).
  - Business layer (BO, src/main/java/com/blog/bo): transactional services, orchestrates DAOs, constructs Response objects, handles auth/token creation and password hashing.
  - DAO layer (src/main/java/com/blog/dao): PanacheRepository-based repositories (implements interfaces like IUserDAO/IBlogDAO).
  - Entities and DTOs (src/main/java/com/blog/model): JPA entities use Lombok and Panache base; DTOs for API payloads.
  - Web assets and templates: `src/main/resources/templates` (Qute) and static JS/CSS under `src/main/resources/META-INF/resources`.
- Persistence: PostgreSQL configured in application.properties. Hibernate ORM with `database.generation=update` (entities will auto-create/update tables).
- Security: BCrypt for password hashing (at.favre.lib), SmallRye JWT for token creation.

---

## Key conventions and patterns (repository-specific)

- Templates vs API: Controllers return HTML via `@Produces(TEXT_HTML)` and expose JSON endpoints separately (e.g., `/api/posts`, `/blog/data`). Keep template-rendering and JSON endpoints separated.
- Naming: classes use suffixes: Controller, BO, DAO, DTO, and entity classes under `model.entity`.
- BO responsibilities: validation, transaction boundaries (`@Transactional`), building Response objects, and calling DAOs — controllers should remain thin.
- DAO pattern: Concrete DAO classes implement an interface and extend `PanacheRepository<T>` for convenient queries (e.g., `find("email", email).firstResult()`).
- Entities: use `@PrePersist` to set created/updated timestamps; avoid setting ID fields externally (they are `@GeneratedValue`).
- Passwords & auth: `UserBO` hashes passwords and builds JWT tokens (`Jwt` builder). To change token claims/expiration, update `UserBO`.
- Local DB requirement: `application.properties` disables Quarkus DevServices (`quarkus.datasource.devservices.enabled=false`) — ensure a local Postgres instance is running when developing.

---

## Important files/locations (for Copilot to reference quickly)

- Main configs: `pom.xml`, `src/main/resources/application.properties`.
- Templates: `src/main/resources/templates/*.html`.
- Static web assets: `src/main/resources/META-INF/resources/js` and `.../css` (served by web-bundler).
- Controllers: `src/main/java/com/blog/controller`.
- Business logic: `src/main/java/com/blog/bo`.
- DAOs: `src/main/java/com/blog/dao`.
- Entities and DTOs: `src/main/java/com/blog/model`.

---

## Existing AI / assistant config files checked

- None of the following were present: `CLAUDE.md`, `.cursorrules`, `.windsurfrules`, `AGENTS.md`, `CONVENTIONS.md`, `.clinerules`.

---

If this file needs to be extended (more commands, CI, linters, or test targets), update this document so Copilot sessions can pick up the new workflows quickly.
