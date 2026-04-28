# Architecture Overview

## High-level structure

PKH is planned as a monorepo with:
- shared KMP modules
- Android app
- iOS app
- Desktop app
- Web app
- Backend service
- Docs

## Main idea

Business logic should be shared where it makes sense:
- domain models
- use cases
- repositories/contracts
- sync-related logic
- shared UI state logic where practical

## Planned top-level modules

- `composeApp/`
- `iosApp/`
- `server/`
- `shared/`
- `docs/`

## R1 focus

For R1, the implementation focus is:
- shared foundations
- backend auth and item ingestion
- Android + Desktop flows
- library and item organization
