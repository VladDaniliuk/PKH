# ADR 0001 — Use a monorepo for PKH

## Status
Accepted

## Context

PKH is a cross-platform project with multiple clients and a backend. The project also needs tightly coupled planning and architecture documentation.

## Decision

Use a single monorepo containing:
- shared KMP code
- all clients
- backend
- docs

## Consequences

### Positive
- one source of truth for code and docs
- easier cross-cutting refactors
- easier architecture visibility
- simpler planning for a solo pet project

### Negative
- repository structure must stay disciplined
- build tooling may become heavier over time
