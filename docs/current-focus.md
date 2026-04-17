# Current Focus

## Current release

**R1 — Capture + Library MVP**

## Primary objective

Deliver the first end-to-end working flow:
1. user signs in
2. user adds a URL
3. backend creates and processes an item
4. item appears in library
5. item details open
6. collection and tags can be assigned

## Active epics

- EPIC 01 — Foundation & Project Setup
- EPIC 02 — Authentication
- EPIC 03 — Capture URL / Add Item
- EPIC 04 — Library & Collections

## Recommended implementation order

### Step 1 — Foundation
- [ ] repository structure
- [ ] shared KMP module skeleton
- [ ] backend skeleton
- [ ] architecture docs

### Step 2 — Authentication vertical slice
- [ ] backend auth endpoints
- [ ] token/session flow
- [ ] shared auth models/use cases
- [ ] Android sign-in screen
- [ ] Desktop sign-in screen

### Step 3 — Add item vertical slice
- [ ] item model and processing states
- [ ] create-item endpoint
- [ ] metadata extraction pipeline
- [ ] add-item shared use case
- [ ] add-item UI

### Step 4 — Library vertical slice
- [ ] library endpoint
- [ ] item details endpoint
- [ ] shared repository layer
- [ ] library screen
- [ ] item details screen

### Step 5 — Collections and tags
- [ ] collections/tags models
- [ ] backend CRUD
- [ ] shared repositories
- [ ] management UI
- [ ] item edit actions

## Scope guard

Do not add to R1 unless it is necessary for the main end-to-end loop.

Explicitly out of current focus:
- advanced reader
- highlights
- notes editor
- search
- smart collections
- import/export
- full sync/offline-first scope
