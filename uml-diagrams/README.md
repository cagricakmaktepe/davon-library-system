# Davon Library Management System - UML Diagrams

This directory contains all the UML diagrams for the Davon Library Management System in Mermaid format.

## Diagram Files

### Class Diagrams
1. **01-user-management-class-diagram.mmd** - User/Member Management Domain
2. **02-book-inventory-class-diagram.mmd** - Book and Library Inventory Domain  
3. **03-borrowing-transactions-class-diagram.mmd** - Borrowing and Transactions Domain

### Use Case Diagrams
4. **04-authentication-usecase-diagram.mmd** - User Registration and Authentication Flows
5. **05-admin-workflows-usecase-diagram.mmd** - Librarian (Admin) Workflows
6. **06-book-management-usecase-diagram.mmd** - Book Management Processes
7. **07-borrowing-returning-usecase-diagram.mmd** - Borrowing and Returning Processes

## How to Use These Files

### Option 1: Online Mermaid Editor
1. Go to [Mermaid Live Editor](https://mermaid.live/)
2. Copy the content from any `.mmd` file
3. Paste it into the editor
4. Export as PNG, SVG, or PDF

### Option 2: VS Code Extension
1. Install the "Mermaid Markdown Syntax Highlighting" extension
2. Install the "Mermaid Preview" extension
3. Open any `.mmd` file and use the preview feature
4. Export diagrams directly from VS Code

### Option 3: Command Line Tool
```bash
# Install mermaid CLI
npm install -g @mermaid-js/mermaid-cli

# Generate PNG from mermaid file
mmdc -i 01-user-management-class-diagram.mmd -o user-management-class.png

# Generate SVG
mmdc -i 01-user-management-class-diagram.mmd -o user-management-class.svg -f svg

# Generate PDF
mmdc -i 01-user-management-class-diagram.mmd -o user-management-class.pdf -f pdf
```

### Option 4: GitHub/GitLab Integration
- Many Git platforms now support Mermaid diagrams natively
- Simply include the `.mmd` files in your repository
- They will render automatically in markdown files

## Diagram Overview

### Class Diagrams
- **User Management**: Handles authentication, sessions, notifications, and user roles
- **Book Inventory**: Manages books, categories, search functionality, and reading lists  
- **Borrowing Transactions**: Covers borrowing, reservations, penalties, renewals, and returns

### Use Case Diagrams
- **Authentication**: Registration, login, profile management, session handling
- **Admin Workflows**: Book/member management, reporting, system monitoring
- **Book Management**: Search, browse, categorization, availability checking
- **Borrowing/Returning**: Complete transaction lifecycle from borrow to return

## Business Rules Captured

### Key Features
- 2-book borrowing limit per member
- Page-based loan periods (14/21 days)
- Progressive penalty system (0.5 TL → 1 TL)
- FIFO reservation queue (max 5 per member)
- Single renewal per book (last 3 days only)
- 100 TL penalty threshold with warnings

### User Roles
- **Member**: Browse, borrow, reserve, manage reading list
- **Admin**: Full system management, reporting, member oversight

### Technical Constraints
- Java/Quarkus backend with MSSQL database
- 50 concurrent user support
- Scalable architecture design
- Session management (30-min timeout, 7-day remember-me)

## Implementation Reference

These diagrams serve as the foundation for:
1. Database schema design
2. Java entity class creation
3. Service layer architecture
4. API endpoint structure
5. Frontend component design

For detailed implementation guidance, see the main **UML_Design_Documentation.md** file.