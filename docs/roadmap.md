# Roadmap

## Planning model

This project is planned through release documents, epic files, and current-focus notes stored in `/docs`.

## Release sequence

### R1 — Capture + Library MVP
Goal: validate the end-to-end flow from authentication to saved knowledge item management.

Includes:
- foundation and project setup
- authentication
- add item by URL
- backend metadata extraction
- library list
- item details
- collections and tags
- Android + Desktop first delivery

### R2 — Reader + Notes
Goal: make saved items genuinely useful for reading and early knowledge extraction.

Likely includes:
- reader experience
- reading state
- notes
- highlights
- better item detail presentation
- improved local persistence

### R3 — Search + Smart Organization
Goal: improve retrieval and organization quality.

Likely includes:
- search
- filters
- smart collections
- improved collection and tag workflows
- better item discovery

### R4 — Import / Export + Background Processing
Goal: make the product more practical for real usage.

Likely includes:
- import flows
- export flows
- more robust ingestion jobs
- background sync and processing improvements

### R5 — Resurfacing + Knowledge Graph direction
Goal: turn saved information into a reusable knowledge system.

Likely includes:
- resurfacing and review flows
- related items
- graph-oriented relationships
- early semantic/intelligent discovery features

## Roadmap rules

- release scope should stay small and testable
- new ideas should be added to the relevant epic or future release doc
- if a feature does not help validate the current release goal, move it to the next release
- architecture decisions that affect multiple releases should be captured in ADR files
