# command-social-backend

## RBAC (Role Based Access Control)

This project now supports a basic RBAC model using **Spring Security roles**.

### What changed

- **User roles persisted in DB**
  - `User` now has `Set<String> roles` stored via `@ElementCollection` in a `user_roles` table.
  - New users default to `ROLE_USER`.

- **Spring Security method security enabled**
  - `@EnableMethodSecurity` is enabled.
  - Controllers use `@PreAuthorize(...)` to enforce permissions.

- **JWT contains roles claim (optional)**
  - When generating a token, roles are also added to JWT claims (`roles`).
  - Authorization is still evaluated from `CustomUserDetails.getAuthorities()`.

### Roles

- `ROLE_USER`
  - Default role for all newly created users.
  - Required for write operations (create/update/delete posts, comments, follow/unfollow, etc.).

- `ROLE_ADMIN`
  - Elevated role.
  - Can list all users and delete users.
  - Can perform owner actions as well (where owner checks exist).

### Endpoint policy summary

- Public (no auth):
  - `POST /api/auth/login`, `POST /api/auth/register`, `GET /api/health`
  - `GET /api/users/**`, `GET /api/posts/**`, `GET /api/comments/**`, `GET /api/search/**`

- Requires authentication + role checks (`@PreAuthorize`):
  - User updates: only the same user (matching `{id}`) or ADMIN
  - User listing / delete: ADMIN only
  - Posts/comments write actions: USER or ADMIN
  - Follow/unfollow: USER or ADMIN
  - Projects: creating a project under `/users/{userId}` requires owner or ADMIN

### Making an admin

Because this project currently has no "admin management" endpoint, you can promote a user by updating the DB directly:

```sql
-- Example
INSERT INTO user_roles(user_id, role) VALUES (1, 'ROLE_ADMIN');
```

(Use the correct `user_id` for the user you want to promote.)

## Error handling

`GlobalExceptionHandler` was expanded to include:

- Spring Security: `AccessDeniedException` (403), `AuthenticationException` / `BadCredentialsException` (401)
- Common request issues: validation errors, missing params, type mismatch, unsupported method


