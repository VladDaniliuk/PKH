# AGENTS.md

## Project
PKH (Personal Knowledge Hub) is a solo pet project.

The repository is planned as a cross-platform system with:
- shared Kotlin Multiplatform modules
- Android app
- iOS app
- Desktop app
- Web app
- backend
- docs-as-code planning inside /docs

## Source of truth
Project planning lives in repository files, not in GitHub Issues.

Always read these files before making product or implementation decisions:
- docs/current-focus.md
- docs/roadmap.md
- docs/releases/*.md
- docs/architecture/*.md
- docs/vision.md
- docs/adr/*.md

When several docs overlap, use this priority:
1. docs/current-focus.md
2. docs/releases/*.md
3. docs/roadmap.md
4. docs/architecture/*.md
5. docs/vision.md
6. docs/adr/*.md

## Planning model
This project uses release-based planning.

Do not invent separate epic structures unless they are explicitly added to the repository later.
The main working planning files are:
- docs/current-focus.md
- docs/releases/*.md

## Working style
- Stay within the current release scope
- Prefer small vertical slices over broad unfinished abstractions
- Do not introduce R2+ functionality while R1 is incomplete
- Keep changes scoped and implementation-oriented
- Prefer editing existing files over creating duplicate planning docs
- Keep docs and implementation aligned

## Architecture expectations
- Shared business logic should stay Kotlin Multiplatform-friendly
- Avoid leaking Android-specific dependencies into shared domain logic unless explicitly intended
- Prefer clear module boundaries
- Prefer practical simplicity over premature abstraction
- For early releases, optimize for a working end-to-end flow over perfect extensibility

## Documentation update rules
Update docs when:
- scope changed
- architecture changed
- release checklist status changed
- a major implementation decision needs to be recorded

Do not update docs just to restate the code.

## Task completion rules
Only mark a checklist item as done when it is actually complete.
Do not mark partial work, spikes, or placeholders as complete.

When a task from docs/releases/*.md is fully completed:
- mark its checkbox
- update related docs if needed
- mention what changed and what remains

## Change discipline
- Avoid unnecessary new files
- Avoid duplicate planning notes
- Avoid hidden scope expansion
- Keep commit-sized changes coherent

## Validation
Before finishing work:
- run the most relevant checks for the touched code
- verify docs still match implementation
- call out assumptions, limitations, and remaining work

## Build and test commands
Current Gradle modules:
- `:shared`
- `:composeApp`
- `:server`

Use these focused validation commands for the current R1.0 foundation:
- `.\gradlew.bat :shared:compileKotlinMetadata`
- `.\gradlew.bat :composeApp:assembleDebug`
- `.\gradlew.bat :composeApp:jvmJar`
- `.\gradlew.bat :server:check`

Notes:
- `:composeApp` contains Android, iOS framework, Desktop JVM, JS browser, and Wasm JS browser targets.
- `iosApp/` is a native Xcode project, not a Gradle module.
- Broad KMP checks such as `.\gradlew.bat :shared:check` may be slow or unsuitable for routine R1.0 validation; prefer the focused commands above unless broader coverage is needed.
