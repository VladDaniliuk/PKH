# ADR 0002 — Use email/password authentication with access and refresh tokens

## Status

Accepted

## Context

PKH needs authentication for R1 so users can access their personal library across clients.

The project is cross-platform, so authentication must work for the Compose Multiplatform client targets and the backend without leaking platform-specific storage details into shared domain code.

R1 also needs a minimal account management baseline:
- registration
- login
- logout
- session restore
- password reset
- password change
- account deletion

## Decision

Use email/password authentication for R1.

The backend issues:
- short-lived access tokens
- long-lived refresh tokens

Access tokens are used for authenticated API requests.

Refresh tokens are opaque random tokens. The backend stores only refresh token hashes, not raw refresh tokens.

Refresh tokens are rotated on refresh. When a refresh token is used successfully, the backend issues a new refresh token and invalidates the previous one.

Clients store tokens in platform-specific secure storage where possible. Shared code depends only on token/session storage abstractions.

Passwords are stored only as password hashes. Prefer Argon2id for password hashing if available in the backend stack.

Password reset uses single-use reset tokens with expiration. The backend stores only reset token hashes.

For R1 development, password reset delivery may be implemented by writing the reset link or token to server logs. Real email delivery can be added later without changing the core reset token model.

Changing password requires the current password and revokes other active sessions for the user.

Deleting an account requires password confirmation. For R1, account deletion may hard-delete user-owned auth data. When items, collections, and tags are implemented, account deletion must also delete or anonymize all user-owned product data.

## Planned backend endpoints

- `POST /auth/register`
- `POST /auth/login`
- `POST /auth/refresh`
- `POST /auth/logout`
- `GET /auth/me`
- `POST /auth/password/forgot`
- `POST /auth/password/reset`
- `POST /auth/password/change`
- `DELETE /account`

## Planned shared abstractions

Backend/client auth contract models live in `:data:auth:contract`:
- `User`
- `AuthTokens`
- `AuthSession`
- `AuthState`

Client-side auth data contracts live in `:data:auth:api`:
- `AuthRepository`
- `TokenStorage`

## Alternatives considered

- Dev-only auth
- Session cookies
- OAuth-only login
- Long-lived access token only
- Email verification in R1
- Multi-factor authentication in R1
- Full session/device management UI in R1

## Consequences

### Positive

- works for Android, Desktop, and backend R1
- keeps API client-friendly
- supports session restore
- gives a path to logout/session invalidation
- supports password reset and password change without changing the core auth model
- keeps platform-specific token storage outside shared domain logic

### Negative

- more complex than dev-only auth
- requires token refresh flow
- requires platform-specific token storage
- requires password hashing and reset token handling on the backend
- requires at least development-mode reset token delivery before real email delivery is added
