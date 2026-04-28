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

- `shared/` - Gradle module `:shared`; Kotlin Multiplatform shared code with Android, iOS, JVM, JS, and Wasm JS targets.
- `composeApp/` - Gradle module `:composeApp`; Compose Multiplatform client code with Android app, iOS framework, Desktop JVM, JS browser, and Wasm JS browser targets.
- `server/` - Gradle module `:server`; Ktor JVM backend service depending on `:shared`.
- `iosApp/` - native Xcode iOS app project that consumes the Compose/iOS framework; it is not a Gradle module.
- `docs/` - product, release, architecture, and ADR documentation.

## R1 focus

For R1, the implementation focus is:
- shared foundations
- backend auth and item ingestion
- Android and Desktop flows
- library and item organization

R1.0 is limited to project foundation work: keeping the repository structure, documentation, and validation commands aligned before feature implementation starts.
