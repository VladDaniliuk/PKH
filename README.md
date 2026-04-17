# PKH — Personal Knowledge Hub

Cross-platform Personal Knowledge Hub built as a KMP pet project with Android, iOS, Desktop, Web, and Backend.

## Purpose

This repository stores both the codebase and the project documentation in markdown files.
The documentation is the source of truth for product scope, roadmap, architecture, ADRs, and current implementation focus.

## Documentation

- [Docs index](docs/README.md)
- [Vision](docs/vision.md)
- [Roadmap](docs/roadmap.md)
- [Current focus](docs/current-focus.md)
- [R1 — Capture + Library MVP](docs/releases/r1-mvp.md)
- [Architecture overview](docs/architecture/overview.md)

## Planning approach

This project uses docs-as-code:
- product and technical planning live in `/docs`
- epics are described in markdown files
- progress is tracked through checklists and release documents
- architecture decisions are documented as ADRs

## Initial scope

The first implementation milestone is **R1 — Capture + Library MVP**:
- authentication
- add item by URL
- backend metadata extraction
- library list
- item details
- collections and tags
- Android + Desktop as the first client targets
