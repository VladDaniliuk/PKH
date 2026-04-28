# ADR 0002 — Use email/password authentication with access and refresh tokens

## Status
Accepted

## Context

PKH needs authentication for R1 so users can access their personal library across clients.

## Decision

Use email/password authentication for R1.
The backend issues:
- short-lived access token
- long-lived refresh token

Refresh tokens are stored on the backend as hashes.
Clients store tokens in platform-specific secure storage where possible.

## Alternatives considered

- Dev-only auth
- Session cookies
- OAuth-only login
- Long-lived access token only

## Consequences

### Positive
- works for Android/Desktop/backend R1
- keeps API client-friendly
- supports session restore
- gives a path to logout/session invalidation

### Negative
- more complex than dev-only auth
- requires token refresh flow
- requires platform-specific token storage
