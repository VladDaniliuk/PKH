# AGENTS.md

## Purpose
The /docs directory is the planning and architecture layer of the project.

These files are meant to be kept concise, practical, and synchronized with the actual implementation.

## Main file roles
- vision.md: product purpose and problem statement
- roadmap.md: high-level release sequence
- current-focus.md: what is being worked on now
- releases/*.md: release scope, checklist, and exit criteria
- architecture/*.md: technical structure and implementation direction
- adr/*.md: important architecture decisions and their reasoning

## Editing rules
- Do not create duplicate docs for the same purpose
- Prefer updating an existing release file over creating side notes
- Prefer updating current-focus.md over scattering temporary TODO files
- Keep release files actionable
- Keep architecture files technical and concrete
- Keep ADRs short and decision-oriented

## Writing style
- Be concrete
- Be implementation-oriented
- Keep sections short
- Prefer checklists for release execution
- Prefer short rationale over long theory
- Avoid vague placeholders like "improve architecture" or "support future scaling" without specifics

## Release file rules
Release files are the main execution documents.

A release file should contain:
- objective
- scope
- out of scope
- exit criteria
- implementation checklist

Do not turn release files into design essays.

## Checklist policy
Mark a checkbox as done only when the work is actually complete.
Do not mark items done for partial implementation, scaffolding only, or exploratory research.

## ADR policy
Create or update an ADR only when:
- a meaningful architecture decision was made
- alternatives were considered
- the decision affects future implementation

Do not create ADRs for trivial local choices.

## Consistency rules
When docs are changed:
- keep naming consistent across files
- keep release scope aligned with current-focus.md
- keep architecture docs aligned with real code structure
