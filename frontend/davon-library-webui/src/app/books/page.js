'use client';

import { useEffect, useState } from 'react';
import AppLayout from '@/components/AppLayout';
import { useUser } from '@/context/UserContext';

export default function BooksPage() {
  const [books, setBooks] = useState([]);
  const [query, setQuery] = useState('');
  const { currentUser } = useUser();

  const loadBooks = async (q = '') => {
    const url = q ? `http://localhost:8080/api/books?q=${encodeURIComponent(q)}` : 'http://localhost:8080/api/books';
    const res = await fetch(url);
    const data = await res.json();
    setBooks(data);
  };

  useEffect(() => {
    loadBooks();
  }, []);

  const checkout = async (bookId) => {
    if (!currentUser) return alert('Please login first');
    const res = await fetch('http://localhost:8080/api/loans', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ userId: currentUser.id, bookId })
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
    const res = await fetch('http://localhost:8080/api/reservations', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ userId: currentUser.id, bookId })
    });
    if (res.ok) {
      alert('Reserved successfully');
    } else {
      const text = await res.text();
      alert(`Reservation failed: ${text}`);
    }
  };

  return (
    <AppLayout>
      <h2>Books</h2>
      <div style={{ marginBottom: 12 }}>
        <input value={query} onChange={e => setQuery(e.target.value)} placeholder="Search title/author" />
        <button onClick={() => loadBooks(query)}>Search</button>
      </div>
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
                <button onClick={() => checkout(b.id)} disabled={b.availableCopies <= 0}>Checkout</button>
                <button onClick={() => reserve(b.id)} disabled={b.availableCopies > 0} style={{ marginLeft: 8 }}>Reserve</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </AppLayout>
  );
}


