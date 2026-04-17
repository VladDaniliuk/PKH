# Vision

## Product name

**PKH — Personal Knowledge Hub**

## Idea

PKH is a cross-platform application for saving, organizing, reading, and reusing knowledge from different sources:
- articles
- documentation pages
- PDFs
- videos
- GitHub issues/discussions
- personal notes

The project is built as a Kotlin Multiplatform + Compose Multiplatform pet project with a dedicated backend.

## Problem

Useful information is usually scattered across many places:
- browser bookmarks
- chat messages
- temporary notes
- open tabs
- GitHub saved items
- PDF files on disk

As a result, knowledge is saved but not structured, not reviewed, and often not reused.

## Product goal

Create a single cross-platform knowledge inbox and library where a user can:
1. save a knowledge item
2. let the backend normalize and enrich it
3. organize it into collections and tags
4. read or review it later from any device
5. progressively turn saved information into reusable knowledge

## Platform roles

### Android / iOS
- quick capture
- reading on the go
- fast triage of saved items
- later reminders and lightweight review flows

### Desktop
- primary organization workspace
- deeper reading and note-taking
- collection management
- richer information density and productivity-oriented UI

### Web
- simple access point
- lightweight reading and onboarding entry
- shareable/public entry points in later releases

### Backend
- authentication
- sync
- URL ingestion and metadata extraction
- item storage
- search indexing
- background processing

## Principles

- docs-as-code
- shared business logic first
- backend that solves real product problems, not a fake demo backend
- release-driven scope control
- start from a narrow MVP and expand gradually

## MVP direction

The first release is **R1 — Capture + Library MVP**.

R1 should prove the end-to-end product loop:
- sign in
- add URL
- backend processes metadata
- item appears in library
- item details open
- collection and tags can be assigned

## Non-goals for early development

- building a full Notion/Obsidian competitor
- advanced AI features from day one
- perfect parsing for every content type
- implementing all platforms equally from the start
- over-engineering the planning process
