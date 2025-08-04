# Davon Library Management System - UML Design Documentation

## Table of Contents
1. [Overview](#overview)
2. [Design Principles](#design-principles)
3. [Class Diagram Design Decisions](#class-diagram-design-decisions)
4. [Use Case Diagram Design Decisions](#use-case-diagram-design-decisions)
5. [System Architecture Considerations](#system-architecture-considerations)
6. [Implementation Plan](#implementation-plan)

## Overview

This document explains the design decisions made during the UML modeling phase of the Davon Library Management System. The system is designed as a full-stack web application using Java/Quarkus backend, React/Next.js frontend, and MSSQL database.

## Design Principles

### 1. **Single Responsibility Principle**
- Each class has a clearly defined responsibility
- User management, book management, and transaction management are separated into distinct domains
- Specialized classes like `PenaltyCalculator` handle specific business logic

### 2. **Open/Closed Principle**
- Abstract `User` class allows extension for new user types without modification
- Enum types for status and notification types allow easy extension
- Modular design supports future feature additions

### 3. **Interface Segregation**
- Clear separation between Member and Admin interfaces
- Domain-specific method groupings in classes
- Specialized classes for specific operations (e.g., `BookSearch`, `UserSession`)

### 4. **Domain-Driven Design**
- Three clear domains: User Management, Book Inventory, Borrowing Transactions
- Rich domain models with business logic encapsulated within entities
- Clear boundaries between domains with minimal coupling

## Class Diagram Design Decisions

### User/Member Management Domain

#### **User Hierarchy Design**
```
User (Abstract)
├── Member
└── Admin
```

**Decision Rationale:**
- **Inheritance over Composition**: Common user attributes (email, password, profile) are shared
- **Role-based Access**: Different capabilities are clearly separated
- **Extensibility**: New user types can be added easily (e.g., Librarian, SuperAdmin)

#### **Session Management**
- **Separate UserSession Class**: Enables multiple concurrent sessions per user
- **Session Timeout Handling**: Built-in session validation and refresh mechanisms
- **Remember Me Feature**: Extended session support for user convenience

#### **Notification System**
- **Dual Channel Support**: Email + in-app notifications
- **Type-based Notifications**: Enum-driven notification categorization
- **Read/Unread Tracking**: Status management for user experience

### Book and Library Inventory Domain

#### **Book Model Design**
```
Book -> Category (Many-to-One)
Book -> BookCopyManager (One-to-One)
```

**Decision Rationale:**
- **Normalized Category Structure**: Dewey Decimal system as separate entity for consistency
- **Copy Management Separation**: Dedicated class for tracking availability vs total copies
- **Page-based Loan Calculation**: Business rule for different loan periods based on book size

#### **Search Functionality**
- **Dedicated BookSearch Class**: Complex search operations with performance tracking
- **Multiple Search Types**: Title, Author, Category, and keyword-based searches
- **Availability Filtering**: Real-time availability checking

#### **Reading List Management**
- **Separate Entity**: Personal book collections independent of borrowing
- **Priority System**: User-defined ordering for reading preferences
- **Completion Tracking**: Progress monitoring for reading goals

### Borrowing and Transactions Domain

#### **Transaction Lifecycle**
```
BorrowRecord (Core Entity)
├── Renewal (Optional)
├── ReturnRecord (Terminal)
└── Penalty (Conditional)
```

**Decision Rationale:**
- **State-based Design**: Clear transaction states with proper transitions
- **Penalty Calculation Logic**: Separate calculator for complex progressive fine system
- **Audit Trail**: Complete history tracking for all borrowing activities

#### **Reservation Queue System**
- **FIFO Queue Implementation**: Fair first-come-first-served system
- **Position Tracking**: Real-time queue position updates
- **Expiry Mechanism**: Time-limited claims to prevent indefinite holds

#### **Progressive Penalty System**
- **Configurable Rate Structure**: 0.5 TL (0-30 days) → 1 TL (30+ days)
- **Threshold Management**: 100 TL warning system with account protection
- **Payment Tracking**: Manual payment marking by administrators

## Use Case Diagram Design Decisions

### Authentication and Authorization Flow

**Multi-level Security Design:**
- **Registration Validation**: Email format and password strength requirements
- **Session Management**: 30-minute idle timeout with remember-me option
- **Role-based Access**: Clear separation between member and admin capabilities

### Admin Workflow Optimization

**Comprehensive Management Interface:**
- **Centralized Book Management**: CRUD operations with copy count management
- **Member Oversight**: Profile viewing, penalty management, account lifecycle
- **Reporting Dashboard**: Statistical analysis and system health monitoring
- **Activity Logging**: Complete audit trail for administrative actions

### Member Experience Design

**User-Centric Interface:**
- **Discovery Tools**: Multiple search and browse options
- **Personal Dashboard**: Borrowed books, due dates, penalties in one view
- **Self-Service Features**: Reading list management, reservation handling
- **Transparency**: Clear queue positions and penalty calculations

### Borrowing Process Workflow

**Business Rule Enforcement:**
- **Limit Validation**: 2-book borrowing limit with penalty threshold checks
- **Dynamic Due Dates**: Page-count-based loan periods (14/21 days)
- **Renewal Logic**: Single renewal with queue validation
- **Availability Management**: Real-time copy tracking and queue notifications

## System Architecture Considerations

### Database Design Implications

**Normalization Strategy:**
- **Third Normal Form**: Minimized redundancy with proper foreign key relationships
- **Indexed Search Fields**: Title, Author, ISBN for performance optimization
- **Audit Tables**: Change tracking for critical business entities

### Performance Considerations

**Scalability Design:**
- **Lazy Loading**: Related entities loaded on demand
- **Caching Strategy**: Frequently accessed book and category data
- **Pagination Support**: Large result sets handled efficiently
- **Connection Pooling**: Database connection optimization

### Security Architecture

**Data Protection:**
- **Password Hashing**: Secure credential storage
- **Session Token Management**: Secure session handling
- **Role-based Authorization**: Method-level security controls
- **Input Validation**: SQL injection and XSS prevention

## Implementation Plan

### Phase 1: Core Infrastructure (Weeks 1-2)
1. **Database Schema Creation**
   - User tables (User, Member, Admin)
   - Authentication tables (UserSession)
   - Basic audit logging

2. **Authentication Service**
   - User registration and login
   - Session management
   - Password validation

### Phase 2: Book Management (Weeks 3-4)
1. **Book Entity Implementation**
   - CRUD operations for books
   - Category management (Dewey Decimal)
   - Copy count tracking

2. **Search Functionality**
   - Basic search implementation
   - Filtering and pagination
   - Availability checking

### Phase 3: Borrowing System (Weeks 5-6)
1. **Borrowing Logic**
   - Borrow record creation
   - Due date calculation
   - Copy availability updates

2. **Reservation System**
   - Queue management
   - Position tracking
   - Notification triggers

### Phase 4: Advanced Features (Weeks 7-8)
1. **Penalty System**
   - Progressive fine calculation
   - Payment tracking
   - Warning system

2. **Renewal and Returns**
   - Renewal eligibility checking
   - Return processing
   - Late fee calculation

### Phase 5: Admin Tools & Reporting (Weeks 9-10)
1. **Administrative Interface**
   - Member management
   - System reports
   - Activity monitoring

2. **Notification System**
   - Email integration
   - In-app notifications
   - Automated reminders

### Phase 6: Frontend Integration (Weeks 11-12)
1. **React Component Development**
   - User authentication UI
   - Book search and display
   - Member dashboard

2. **Admin Dashboard**
   - Book management interface
   - Reporting tools
   - System administration

### Testing Strategy

**Unit Testing (Throughout Development):**
- Service layer business logic
- Repository data access patterns
- Utility class functionality

**Integration Testing (Phases 4-5):**
- API endpoint validation
- Database transaction testing
- Cross-service communication

**End-to-End Testing (Phase 6):**
- Complete user workflows
- Admin operation flows
- Error handling scenarios

### Deployment Considerations

**Containerization Strategy:**
- Docker containers for each service
- Docker Compose for local development
- Environment-specific configurations

**CI/CD Pipeline:**
- GitHub Actions for automated testing
- Automated deployment to staging
- Production deployment approval gates

---

## Conclusion

This UML design provides a solid foundation for implementing the Davon Library Management System. The modular architecture supports incremental development while ensuring scalability and maintainability. The clear separation of concerns between domains facilitates parallel development and makes the system easier to understand and modify.

The design balances business requirements with technical best practices, ensuring that the system can handle the specified load (50 concurrent users) while remaining extensible for future enhancements.