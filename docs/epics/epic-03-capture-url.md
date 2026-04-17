# EPIC 03 — Capture URL / Add Item

## Goal

Allow the user to submit a URL and create a knowledge item that goes through a minimal processing flow.

## In scope

- item domain model
- create item endpoint
- metadata extraction
- add-item UI
- processing states

## Out of scope

- advanced content parsing
- full reader
- browser import

## Tasks

- [ ] Define item domain model and processing states
- [ ] Implement backend endpoint to create item from URL
- [ ] Implement backend metadata extraction pipeline
- [ ] Store extracted metadata for saved URL
- [ ] Add shared use case for adding item by URL
- [ ] Implement add item by URL screen/dialog
- [ ] Show processing, ready and failed item states
- [ ] Prevent duplicate submission while request is in progress
- [ ] Define minimal metadata extraction strategy for R1
