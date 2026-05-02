# R1 — Capture + Library MVP

## Objective

Deliver a working end-to-end flow:
- user signs in
- user adds a URL
- backend processes metadata
- item appears in library
- user opens item details
- user assigns collection and tags

## Scope

- Project foundation
- Authentication
- Account management basics
- Add item by URL
- Item processing state
- Library list
- Item details
- Collections and tags

## Out of scope

- Reader experience
- Highlights
- Notes
- Search
- Smart collections
- Web/iOS feature completeness
- OAuth / social login
- Multi-factor authentication
- Email verification
- Device/session management UI

## Exit criteria

- [ ] User can sign in
- [ ] User can add a URL
- [ ] Backend creates item in processing state
- [ ] Metadata processing is reflected in UI
- [ ] User can see items in library
- [ ] User can open item details
- [ ] User can create collections and tags
- [ ] User can assign collections and tags to an item

## Implementation checklist

### R1.0 — Project foundation

- [x] Confirm actual module structure
- [x] Update architecture overview if module structure changed
- [x] Add project build/test commands to `AGENTS.md`
- [x] Fix broken docs links
- [x] Verify shared module builds
- [x] Verify Android app builds
- [x] Verify Desktop app builds, if already present
- [x] Verify backend module builds, if already present

### R1.1 — Authentication & Account MVP

#### Core authentication

- [x] Confirm R1 authentication strategy from ADR 0002
- [x] Add shared auth domain models
- [x] Add `AuthRepository` contract
- [x] Add client token/session storage abstraction
- [x] Add backend user model
- [x] Add backend auth session model
- [x] Add backend password hashing
- [x] Add access token issuing and validation
- [x] Add refresh token generation, hashing, validation, and rotation
- [ ] Add register endpoint
- [ ] Add login endpoint
- [ ] Add refresh session endpoint
- [ ] Add logout endpoint
- [ ] Add current user endpoint
- [ ] Add authenticated API request support on client
- [ ] Add Android login/register screen
- [ ] Restore auth session after app restart
- [ ] Route unauthenticated user to auth flow
- [ ] Route authenticated user to library flow

#### Account management

- [ ] Add password reset token model
- [ ] Add forgot password endpoint
- [ ] Add reset password endpoint
- [ ] Add dev-mode password reset delivery through server logs
- [ ] Add change password endpoint
- [ ] Revoke other sessions after password change
- [ ] Add delete account endpoint
- [ ] Require password confirmation before account deletion
- [ ] Delete or anonymize user-owned auth data after account deletion
- [ ] Clear local tokens after account deletion
- [ ] Add basic UI for forgot password
- [ ] Add basic UI for reset password
- [ ] Add basic UI for change password
- [ ] Add basic UI for delete account

### R1.2 — Shared item domain contracts

- [ ] Add `KnowledgeItem` domain model
- [ ] Add item processing status model
- [ ] Add collection domain model
- [ ] Add tag domain model
- [ ] Add `ItemRepository` contract
- [ ] Add item-related use cases where useful
- [ ] Keep shared domain free from Android-specific dependencies

### R1.3 — Add item by URL

- [ ] Add shared use case for adding URL
- [ ] Add backend item creation model
- [ ] Add backend endpoint for item creation
- [ ] Validate URL input
- [ ] Create item with processing status
- [ ] Return created item to client
- [ ] Show add URL form in Android app
- [ ] Show add URL loading state
- [ ] Show add URL error state
- [ ] Navigate or update UI after successful item creation

### R1.4 — Metadata processing state

- [ ] Add basic metadata processing flow
- [ ] Update item from processing state to ready state
- [ ] Support failed processing state
- [ ] Expose item processing status through backend API
- [ ] Reflect processing state in library UI
- [ ] Reflect processing state in item details UI

### R1.5 — Library list

- [ ] Add backend endpoint for item list
- [ ] Add repository method for loading or observing library items
- [ ] Add Android library screen
- [ ] Show library loading state
- [ ] Show library empty state
- [ ] Show library error state
- [ ] Show item title, URL, status, and basic metadata
- [ ] Navigate from library item to item details

### R1.6 — Item details

- [ ] Add backend endpoint for item details
- [ ] Add repository method for loading item details
- [ ] Add Android item details screen
- [ ] Show item title
- [ ] Show source URL
- [ ] Show processing status
- [ ] Show available metadata
- [ ] Handle details loading state
- [ ] Handle details error state

### R1.7 — Collections and tags

- [ ] Add backend collection model
- [ ] Add backend tag model
- [ ] Add create collection endpoint
- [ ] Add create tag endpoint
- [ ] Add assign collections and tags to item endpoint
- [ ] Add repository methods for collections and tags
- [ ] Add create collection UI
- [ ] Add create tag UI
- [ ] Add assign collections/tags UI in item details
- [ ] Show assigned collections/tags in item details
- [ ] Show assigned collections/tags in library item if useful

### R1.8 — Desktop validation

- [ ] Decide minimal Desktop scope for R1
- [ ] Reuse shared auth/item contracts from Desktop client
- [ ] Add minimal Desktop auth flow, if included in R1
- [ ] Add minimal Desktop library flow, if included in R1
- [ ] Verify shared logic is not Android-only

### R1.9 — R1 stabilization

- [ ] Run relevant checks for touched modules
- [ ] Verify docs match implementation
- [ ] Verify R1 exit criteria manually
- [ ] Remove temporary fake data if real backend flow is ready
- [ ] Keep fake implementations only if they are clearly marked for development/testing
- [ ] Mark exit criteria as done only when the user-facing flow is fully complete
