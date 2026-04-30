# Architecture Overview

## High-level structure

PKH is a monorepo with:
- shared Kotlin Multiplatform code
- Compose Multiplatform app targets
- native iOS app project
- backend service
- docs-as-code planning

## Main idea

Business logic should be shared where it makes sense:
- domain models
- use cases
- repositories/contracts
- sync-related logic
- shared UI state logic where practical

## Current repository structure

- `shared/` - Gradle module `:shared`; Kotlin Multiplatform entry module that aggregates shared domain/data API modules for clients.
- `domain/` - Kotlin Multiplatform domain modules. Domain modules contain use cases, validators, and platform-free business rules. They may depend on `data:*:api` modules only.
- `data/*/contract/` - Kotlin Multiplatform contract modules. They contain API/domain models shared between the backend and client applications for backend communication.
- `data/*/api/` - Kotlin Multiplatform data API modules. They contain client-facing repository/storage contracts and API-layer errors.
- `data/*/impl/` - Kotlin Multiplatform data implementation modules. They contain repository implementations and datasource wiring for clients.
- `composeApp/` - Gradle module `:composeApp`; Compose Multiplatform client code with Android app, iOS framework, Desktop JVM, and Wasm JS browser targets.
- `server/` - Gradle module `:server`; Ktor JVM backend service depending on narrow shared contract modules where backend/client API models need to be reused.
- `iosApp/` - native Xcode iOS app project that consumes the Compose/iOS framework; it is not a Gradle module.
- `build-logic/` - included Gradle build with local build tooling, including the `local-dependency-graph` plugin.
- `docs/` - product, release, architecture, and ADR documentation.

## Module dependency direction

- Feature modules own UI and depend on domain modules.
- Domain modules depend only on data API modules.
- Data API modules may depend on data contract modules.
- Data contract modules define backend/client API models.
- Data API modules define client-facing repository/storage contracts.
- Data implementation modules implement data API contracts and own datasource wiring.
- The `:shared` module is the client-facing shared entrypoint, not a dependency for backend code and not a place for feature-specific domain implementation.
- Shared and domain code must stay platform-free; Android-specific dependencies belong in Android source sets or app/feature integration code.

## Shared models and errors

- Reuse common contract models from `data:*:contract` across clients and the server when they describe the same backend API concept.
- Keep client-only interfaces, such as repositories and token storage, in `data:*:api`.
- Do not reuse persistence records, database tables, or platform-specific DTOs as shared API/domain models.
- Map between shared contract models and local persistence/platform models at module boundaries.
- Repository contracts and domain use cases return `Result<T>` for recoverable failures.
- Feature/UI layers should handle typed domain/data errors instead of parsing exception messages.
- UI-safe validation errors may describe local input problems, such as invalid email or weak password.
- Authentication failures from the backend must stay generic, such as invalid credentials, to avoid leaking whether an email exists or which credential failed.
- Token storage uses explicit empty/available states instead of nullable success values.
- Access-token refresh is data-layer infrastructure handled by repository/API client code, not a UI/domain use case.

## Gradle module dependency graph

The root project applies the local `local-dependency-graph` Gradle plugin from the included `build-logic` build.

Run `.\gradlew.bat generateModuleDependencyGraph` to generate `docs/architecture/module-dependency-graph.mmd`.
The generated Mermaid diagram contains only internal Gradle project dependencies. External Maven and Gradle dependencies are intentionally omitted.
Edges point from a module to the internal module it depends on, and each edge label lists the Gradle configurations where that dependency is declared.
The diagram groups modules by architectural layer, such as feature, domain, data API, data implementation, data contract, and backend modules.

## Dependency injection

PKH uses Koin for dependency injection across shared KMP code, Compose clients, and the Ktor server.
DI definitions should use Koin annotations and the Koin compiler plugin instead of handwritten Koin DSL modules.
Dependencies supplied by a different architectural layer, such as repository implementations or runtime server configuration, should be marked as provided at the annotation boundary.

## R1 focus

For R1, the implementation focus is:
- shared foundations
- backend auth and item ingestion
- Android and Desktop flows
- library and item organization

R1.0 is limited to project foundation work: keeping the repository structure, documentation, and validation commands aligned before feature implementation starts.
