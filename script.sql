BEGIN;

-- Authors
CREATE TABLE authors (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

-- Users (avoid reserved keyword "user")
CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE
);

-- Books
CREATE TABLE books (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  author_id BIGINT NOT NULL REFERENCES authors(id) ON DELETE RESTRICT,
  isbn VARCHAR(20) NOT NULL UNIQUE,
  total_copies INTEGER NOT NULL CHECK (total_copies >= 0),
  available_copies INTEGER NOT NULL CHECK (available_copies >= 0 AND available_copies <= total_copies),
  page_count INTEGER NOT NULL CHECK (page_count >= 0)
);

-- Loans
CREATE TABLE loans (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
  book_id BIGINT NOT NULL REFERENCES books(id) ON DELETE RESTRICT,
  checkout_date DATE NOT NULL,
  due_date DATE NOT NULL,
  return_date DATE,
  CHECK (due_date >= checkout_date),
  CHECK (return_date IS NULL OR return_date >= checkout_date)
);

-- Reservations
CREATE TABLE reservations (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
  book_id BIGINT NOT NULL REFERENCES books(id) ON DELETE RESTRICT,
  reservation_date DATE NOT NULL,
  expiry_date DATE,
  queue_position INTEGER,
  status VARCHAR(20) NOT NULL,
  CHECK (queue_position IS NULL OR queue_position > 0),
  CHECK (status IN ('WAITING', 'AVAILABLE', 'COMPLETED', 'CANCELLED', 'EXPIRED'))
);

-- Notifications
CREATE TABLE notifications (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  type VARCHAR(100) NOT NULL,
  title VARCHAR(255) NOT NULL,
  message TEXT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  read BOOLEAN NOT NULL DEFAULT FALSE
);

-- Indexes
CREATE INDEX idx_authors_name ON authors (name);
CREATE INDEX idx_books_title ON books (title);
CREATE INDEX idx_books_author_id ON books (author_id);

CREATE INDEX idx_loans_user_id ON loans (user_id);
CREATE INDEX idx_loans_book_id ON loans (book_id);

CREATE INDEX idx_reservations_user_id ON reservations (user_id);
CREATE INDEX idx_reservations_book_id ON reservations (book_id);
CREATE INDEX idx_reservations_status ON reservations (status);

CREATE INDEX idx_notifications_user_id ON notifications (user_id);
CREATE INDEX idx_notifications_created_at ON notifications (created_at);

-- Seed data

-- Authors
INSERT INTO authors (id, name) VALUES
  (1, 'Robert C. Martin'),
  (2, 'Joshua Bloch'),
  (3, 'Eric Evans');

-- Users
INSERT INTO users (id, name, email) VALUES
  (1, 'Alice Johnson', 'alice@example.com'),
  (2, 'Bob Smith', 'bob@example.com'),
  (3, 'Charlie Brown', 'charlie@example.com');

-- Books
INSERT INTO books (id, title, author_id, isbn, total_copies, available_copies, page_count) VALUES
  (1, 'Clean Code', 1, '9780132350884', 3, 3, 464),
  (2, 'Effective Java', 2, '9780134685991', 2, 2, 416),
  (3, 'Domain-Driven Design', 3, '9780321125217', 1, 1, 560);

-- Loans
INSERT INTO loans (id, user_id, book_id, checkout_date, due_date, return_date) VALUES
  (1, 1, 1, DATE '2025-01-10', DATE '2025-01-24', DATE '2025-01-20'),
  (2, 2, 2, DATE '2025-02-01', DATE '2025-02-15', DATE '2025-02-10');

-- Reservations
INSERT INTO reservations (id, user_id, book_id, reservation_date, expiry_date, queue_position, status) VALUES
  (1, 3, 3, DATE '2025-03-01', DATE '2025-03-15', 1, 'WAITING'),
  (2, 2, 1, DATE '2025-03-05', DATE '2025-03-20', 1, 'COMPLETED'),
  (3, 1, 2, DATE '2025-03-08', DATE '2025-03-22', 1, 'AVAILABLE');

-- Notifications
INSERT INTO notifications (id, user_id, type, title, message, created_at, read) VALUES
  (1, 1, 'DUE_SOON', 'Loan due soon', 'Your loan for Clean Code is due in 2 days.', CURRENT_TIMESTAMP - INTERVAL '3 days', FALSE),
  (2, 3, 'RESERVATION_AVAILABLE', 'Reservation available', 'Domain-Driven Design is now available for pickup.', CURRENT_TIMESTAMP - INTERVAL '1 day', FALSE),
  (3, 2, 'GENERAL', 'Welcome', 'Welcome to Devon Library System.', CURRENT_TIMESTAMP - INTERVAL '7 days', TRUE);

-- Reset sequences to MAX(id)
SELECT setval(pg_get_serial_sequence('authors', 'id'), (SELECT MAX(id) FROM authors), true);
SELECT setval(pg_get_serial_sequence('users', 'id'), (SELECT MAX(id) FROM users), true);
SELECT setval(pg_get_serial_sequence('books', 'id'), (SELECT MAX(id) FROM books), true);
SELECT setval(pg_get_serial_sequence('loans', 'id'), (SELECT MAX(id) FROM loans), true);
SELECT setval(pg_get_serial_sequence('reservations', 'id'), (SELECT MAX(id) FROM reservations), true);
SELECT setval(pg_get_serial_sequence('notifications', 'id'), (SELECT MAX(id) FROM notifications), true);

COMMIT;