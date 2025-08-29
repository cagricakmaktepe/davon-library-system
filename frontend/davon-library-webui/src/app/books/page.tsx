'use client';

import { useEffect, useState } from 'react';
import AppLayout from '@/components/AppLayout';
import { useUser } from '@/context/UserContext';

export default function BooksPage() {
  const [books, setBooks] = useState([]);
  const [query, setQuery] = useState('');
  const { currentUser, isAdmin, isMember } = useUser();
  const [readingList, setReadingList] = useState([]);
  const [newBook, setNewBook] = useState({
    title: '',
    authorName: '',
    isbn: '',
    pageCount: 100,
    totalCopies: 1
  });

  const loadBooks = async (q = '') => {
    const url = q ? `http://localhost:8080/api/books?q=${encodeURIComponent(q)}` : 'http://localhost:8080/api/books';
    const res = await fetch(url);
    const data = await res.json();
    setBooks(data);
  };
  const handleNewBookChange = (e) => {
    const { name, value } = e.target;
    setNewBook(prev => ({ ...prev, [name]: value }));
  };

  const submitNewBook = async (e) => {
    e.preventDefault();
    if (!isAdmin()) return alert('Admin only');
    const title = newBook.title.trim();
    if (!title) return alert('Title is required');
    const authorName = newBook.authorName.trim();
    const isbn = newBook.isbn.trim();
    const pageCount = parseInt(String(newBook.pageCount), 10) || 0;
    const totalCopies = parseInt(String(newBook.totalCopies), 10) || 0;
    const res = await fetch('http://localhost:8080/api/books', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        title,
        isbn,
        pageCount,
        totalCopies,
        authorName,
        actorUserId: currentUser?.backendId ?? currentUser?.id
      })
    });
    if (res.ok) {
      alert('Book created');
      setNewBook({ title: '', authorName: '', isbn: '', pageCount: 100, totalCopies: 1 });
      loadBooks(query);
    } else {
      alert('Create failed: ' + (await res.text()));
    }
  };

  const updateCopies = async (bookId) => {
    if (!isAdmin()) return alert('Admin only');
    const input = prompt('New total copies');
    if (input == null) return;
    const totalCopies = parseInt(input, 10);
    if (Number.isNaN(totalCopies)) return;
    const res = await fetch(`http://localhost:8080/api/books/${bookId}/copies`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ totalCopies, actorUserId: currentUser.backendId ?? currentUser.id })
    });
    if (res.ok) {
      alert('Copies updated');
      loadBooks(query);
    } else {
      alert('Update failed: ' + (await res.text()));
    }
  };

  const deleteBook = async (bookId) => {
    if (!isAdmin()) return alert('Admin only');
    if (!confirm('Delete this book?')) return;
    const res = await fetch(`http://localhost:8080/api/books/${bookId}/delete`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ actorUserId: currentUser.backendId ?? currentUser.id })
    });
    if (res.ok || res.status === 204) {
      alert('Deleted');
      loadBooks(query);
    } else {
      alert('Delete failed: ' + (await res.text()));
    }
  };


  useEffect(() => {
    loadBooks();
    const loadReadingList = async () => {
      if (!currentUser) return;
      const res = await fetch(`http://localhost:8080/api/reading-list/user/${currentUser.backendId ?? currentUser.id}`);
      if (res.ok) setReadingList(await res.json());
    };
    loadReadingList();
  }, []);

  const checkout = async (bookId) => {
    if (!currentUser) return alert('Please login first');
    const res = await fetch('http://localhost:8080/api/loans', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ userId: currentUser.backendId ?? currentUser.id, bookId })
    });
    if (res.ok) {
      alert('Checked out successfully');
      loadBooks(query);
    } else {
      const text = await res.text();
      alert(`Checkout failed: ${text}`);
    }
  };

  const reserve = async (bookId) => {
    if (!currentUser) return alert('Please login first');
    // Prevent reserve if this member already borrowed this book
    const loansRes = await fetch(`http://localhost:8080/api/loans/user/${currentUser.backendId ?? currentUser.id}`);
    if (loansRes.ok) {
      const loans = await loansRes.json();
      const hasLoan = loans.some(l => l.bookId === bookId && !l.returnDate);
      if (hasLoan) {
        alert('You already borrowed this book');
        return;
      }
    }
    const res = await fetch('http://localhost:8080/api/reservations', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ userId: currentUser.backendId ?? currentUser.id, bookId })
    });
    if (res.ok) {
      alert('Reserved successfully');
    } else {
      const text = await res.text();
      alert(`Reservation failed: ${text}`);
    }
  };

  const addToReadingList = async (bookId) => {
    if (!currentUser) return alert('Please login first');
    const res = await fetch('http://localhost:8080/api/reading-list', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ userId: currentUser.backendId ?? currentUser.id, bookId })
    });
    if (res.ok) {
      const rl = await fetch(`http://localhost:8080/api/reading-list/user/${currentUser.backendId ?? currentUser.id}`);
      if (rl.ok) setReadingList(await rl.json());
    } else {
      alert('Add failed: ' + (await res.text()));
    }
  };

  const removeFromReadingList = async (bookId) => {
    if (!currentUser) return alert('Please login first');
    const res = await fetch('http://localhost:8080/api/reading-list/remove', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ userId: currentUser.backendId ?? currentUser.id, bookId })
    });
    if (res.ok || res.status === 204) {
      const rl = await fetch(`http://localhost:8080/api/reading-list/user/${currentUser.backendId ?? currentUser.id}`);
      if (rl.ok) setReadingList(await rl.json());
    } else {
      alert('Remove failed: ' + (await res.text()));
    }
  };

  return (
    <AppLayout>
      <h2>Books</h2>
      <div style={{ marginBottom: 12 }}>
        <input value={query} onChange={e => setQuery(e.target.value)} placeholder="Search title/author" />
        <button onClick={() => loadBooks(query)}>Search</button>
        {isMember() && (
          <button onClick={() => window.location.href = '/member'} style={{ marginLeft: 8 }}>My Dashboard</button>
        )}
      </div>
      {isAdmin() && (
        <form onSubmit={submitNewBook} style={{ marginBottom: 16, padding: 12, border: '1px solid #eee', borderRadius: 6 }}>
          <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap', alignItems: 'center' }}>
            <input
              name="title"
              value={newBook.title}
              onChange={handleNewBookChange}
              placeholder="Title"
              required
            />
            <input
              name="authorName"
              value={newBook.authorName}
              onChange={handleNewBookChange}
              placeholder="Author Name"
            />
            <input
              name="isbn"
              value={newBook.isbn}
              onChange={handleNewBookChange}
              placeholder="ISBN"
            />
            <input
              name="pageCount"
              type="number"
              min={1}
              value={newBook.pageCount}
              onChange={handleNewBookChange}
              placeholder="Page Count"
            />
            <input
              name="totalCopies"
              type="number"
              min={1}
              value={newBook.totalCopies}
              onChange={handleNewBookChange}
              placeholder="Total Copies"
            />
            <button type="submit">Add Book</button>
          </div>
        </form>
      )}
      <table>
        <thead>
          <tr>
            <th>Title</th>
            <th>Author</th>
            <th>ISBN</th>
            <th>Available</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {books.map(b => (
            <tr key={b.id}>
              <td>{b.title}</td>
              <td>{b.author?.name || '-'}</td>
              <td>{b.isbn}</td>
              <td>{b.availableCopies}/{b.totalCopies}</td>
              <td>
                {isMember() && (
                  <>
                    <button onClick={() => checkout(b.id)} disabled={b.availableCopies <= 0}>Checkout</button>
                    <button onClick={() => reserve(b.id)} disabled={b.availableCopies > 0} style={{ marginLeft: 8 }}>Reserve</button>
                    {readingList.some(i => i.bookId === b.id) ? (
                      <button onClick={() => removeFromReadingList(b.id)} style={{ marginLeft: 8 }}>Remove from Reading List</button>
                    ) : (
                      <button onClick={() => addToReadingList(b.id)} style={{ marginLeft: 8 }}>Add to Reading List</button>
                    )}
                  </>
                )}
                {isAdmin() && (
                  <>
                    <button onClick={() => updateCopies(b.id)}>Set Copies</button>
                    <button onClick={() => deleteBook(b.id)} style={{ marginLeft: 8, color: 'red' }}>Delete</button>
                  </>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </AppLayout>
  );
}


