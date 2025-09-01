'use client';

import { useEffect, useState } from 'react';
import AppLayout from '@/components/AppLayout';
import { useUser } from '@/context/UserContext';

export default function ReservationsPage() {
  const { currentUser } = useUser();
  const [reservations, setReservations] = useState([]);

  const load = async () => {
    if (!currentUser) return;
    const res = await fetch(`http://localhost:8080/api/reservations/user/${currentUser.id}`);
    const data = await res.json();
    setReservations(data);
  };

  useEffect(() => {
    load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentUser?.id]);

  const cancelReservation = async (id) => {
    const res = await fetch(`http://localhost:8080/api/reservations/${id}/cancel`, { method: 'POST' });
    if (res.ok) {
      await load();
      alert('Cancelled');
    } else {
      alert('Failed');
    }
  };

  return (
    <AppLayout>
      <h2>My Reservations</h2>
      {!currentUser ? (
        <p>Please login to view your reservations.</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Book ID</th>
              <th>Status</th>
              <th>Queue</th>
              <th>Reserved</th>
              <th>Expires</th>
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
                <td>{r.reservationDate}</td>
                <td>{r.expiryDate}</td>
                <td>
                  <button onClick={() => cancelReservation(r.id)} disabled={r.status !== 'WAITING' && r.status !== 'AVAILABLE'}>Cancel</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </AppLayout>
  );
}


