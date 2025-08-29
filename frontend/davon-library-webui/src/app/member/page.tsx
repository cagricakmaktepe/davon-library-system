'use client';

import { useEffect, useState } from 'react';
import AppLayout from '@/components/AppLayout';
import { useUser } from '@/context/UserContext';
import { useRouter } from 'next/navigation';

export default function MemberDashboardPage() {
  const { currentUser, isMember } = useUser();
  const router = useRouter();
  const [borrowed, setBorrowed] = useState([]);
  const [reservations, setReservations] = useState([]);
  const [readingList, setReadingList] = useState([]);

  useEffect(() => {
    if (!currentUser) {
      router.push('/users/login');
      return;
    }
    if (!isMember()) {
      router.push('/');
      return;
    }
    const load = async () => {
      const loansRes = await fetch(`http://localhost:8080/api/loans/user/${currentUser.backendId ?? currentUser.id}`);
      if (loansRes.ok) setBorrowed(await loansRes.json());
      const resRes = await fetch(`http://localhost:8080/api/reservations/user/${currentUser.backendId ?? currentUser.id}`);
      if (resRes.ok) setReservations(await resRes.json());
      const rlRes = await fetch(`http://localhost:8080/api/reading-list/user/${currentUser.backendId ?? currentUser.id}`);
      if (rlRes.ok) setReadingList(await rlRes.json());
    };
    load();
  }, [currentUser, isMember, router]);

  const renew = async (loanId) => {
    const res = await fetch(`http://localhost:8080/api/loans/${loanId}/renew`, { method: 'POST' });
    if (res.ok) {
      alert('Renewed');
      const loansRes = await fetch(`http://localhost:8080/api/loans/user/${currentUser.backendId ?? currentUser.id}`);
      if (loansRes.ok) setBorrowed(await loansRes.json());
    } else {
      alert('Renew failed: ' + (await res.text()))
    }
  };

  const removeFromReadingList = async (bookId) => {
    const res = await fetch('http://localhost:8080/api/reading-list/remove', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ userId: currentUser.backendId ?? currentUser.id, bookId })
    });
    if (res.ok || res.status === 204) {
      const rlRes = await fetch(`http://localhost:8080/api/reading-list/user/${currentUser.backendId ?? currentUser.id}`);
      if (rlRes.ok) setReadingList(await rlRes.json());
    }
  };

  const returnBook = async (loanId) => {
    const res = await fetch(`http://localhost:8080/api/loans/${loanId}/return`, { method: 'POST' });
    if (res.ok) {
      alert('Returned');
      const loansRes = await fetch(`http://localhost:8080/api/loans/user/${currentUser.backendId ?? currentUser.id}`);
      if (loansRes.ok) setBorrowed(await loansRes.json());
    }
  };

  const claimReservation = async (reservationId) => {
    const res = await fetch(`http://localhost:8080/api/reservations/${reservationId}/claim`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ userId: currentUser.id })
    });
    if (res.ok) {
      alert('Claimed and checked out');
      const loansRes = await fetch(`http://localhost:8080/api/loans/user/${currentUser.id}`);
      if (loansRes.ok) setBorrowed(await loansRes.json());
      const resRes = await fetch(`http://localhost:8080/api/reservations/user/${currentUser.backendId ?? currentUser.id}`);
      if (resRes.ok) setReservations(await resRes.json());
    } else {
      alert('Claim failed: ' + (await res.text()));
    }
  };

  const cancelReservation = async (reservationId) => {
    const res = await fetch(`http://localhost:8080/api/reservations/${reservationId}/cancel`, { method: 'POST' });
    if (res.ok) {
      alert('Cancelled');
      const resRes = await fetch(`http://localhost:8080/api/reservations/user/${currentUser.id}`);
      if (resRes.ok) setReservations(await resRes.json());
    } else {
      alert('Cancel failed: ' + (await res.text()));
    }
  };

  return (
    <AppLayout>
      <h1>Member Dashboard</h1>

      <section style={{ marginTop: 16 }}>
        <h2>Borrowed Books</h2>
        <table>
          <thead>
            <tr>
              <th>Loan ID</th>
              <th>Book ID</th>
              <th>Checkout</th>
              <th>Due</th>
              <th>Returned</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {borrowed.map(l => (
              <tr key={l.id}>
                <td>{l.id}</td>
                <td>{l.bookId}</td>
                <td>{l.checkoutDate}</td>
                <td>{l.dueDate}</td>
                <td>{l.returnDate ? 'Yes' : 'No'}</td>
                <td>
                  {!l.returnDate && (
                    <>
                      <button onClick={() => renew(l.id)} style={{ marginRight: 8 }}>Renew</button>
                      <button onClick={() => returnBook(l.id)}>Return</button>
                    </>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </section>

      <section style={{ marginTop: 24 }}>
        <h2>Reservations</h2>
        <table>
          <thead>
            <tr>
              <th>Reservation ID</th>
              <th>Book ID</th>
              <th>Status</th>
              <th>Queue Pos</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {reservations.map(r => (
              <tr key={r.id}>
                <td>{r.id}</td>
                <td>{r.bookId}</td>
                <td>{r.status}</td>
                <td>{r.queuePosition}</td>
                <td>
                  {r.status === 'AVAILABLE' && (
                    <button onClick={() => claimReservation(r.id)}>Claim</button>
                  )}
                  {r.status === 'WAITING' && (
                    <button onClick={() => cancelReservation(r.id)} style={{ marginLeft: 8 }}>Cancel</button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </section>

      <section style={{ marginTop: 24 }}>
        <h2>My Reading List</h2>
        <table>
          <thead>
            <tr>
              <th>Item ID</th>
              <th>Book ID</th>
              <th>Priority</th>
              <th>Added</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {readingList.map(i => (
              <tr key={i.id}>
                <td>{i.id}</td>
                <td>{i.bookId}</td>
                <td>{i.priority}</td>
                <td>{i.addedAt}</td>
                <td>
                  <button onClick={() => removeFromReadingList(i.bookId)}>Remove</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </section>
    </AppLayout>
  );
}


