# Davon Library System

## Branching Strategy

This project uses the following Git branching strategy:

- **main**: The stable, production-ready branch.
- **develop**: The integration branch for features and fixes. All feature branches are merged here first.
- **feature/***: Feature branches for new features or tasks (e.g., `feature/login`, `feature/book-search`).

## Workflow

1. **Start from develop**: Always branch off from `develop` for new features or fixes.
2. **Create a feature branch**: Use descriptive names, e.g., `feature/your-feature-name`.
3. **Commit messages**: Use clear, concise, present-tense messages (e.g., "Add user authentication API endpoint").
4. **Pull Requests (PRs)**: Open PRs from your feature branch into `develop`. Include a summary, related issues, and any important notes.
5. **Merging to main**: After testing and review, changes from `develop` are merged into `main` for release.

## Example Commands

```bash
git checkout develop
git pull
git checkout -b feature/your-feature-name
git push -u origin feature/your-feature-name
```

## Contribution Guidelines
- Follow the branching strategy above.
- Write meaningful commit messages and PR descriptions.
- Keep PRs focused and small when possible.