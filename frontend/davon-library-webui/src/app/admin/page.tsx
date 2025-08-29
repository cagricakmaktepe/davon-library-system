'use client';

import { useEffect, useState } from 'react';
import AppLayout from '@/components/AppLayout';
import { useUser } from '@/context/UserContext';
import { useRouter } from 'next/navigation';

export default function AdminDashboardPage() {
  const { currentUser, isAdmin } = useUser();
  const router = useRouter();
  const [stats, setStats] = useState(null);
  const [overdue, setOverdue] = useState([]);
  const [notifyForm, setNotifyForm] = useState({ userId: '', title: '', message: '' });
  const [users, setUsers] = useState([]);

  useEffect(() => {
    if (!currentUser) {
      router.push('/users/login');
      return;
    }
    if (!isAdmin()) {
      router.push('/');
      return;
    }
    const loadStats = async () => {
      const actorId = currentUser.backendId ?? currentUser.id;
      const res = await fetch(`http://localhost:8080/api/reports/stats?actorUserId=${actorId}`);
      if (res.ok) {
        setStats(await res.json());
      }
    };
    const loadOverdue = async () => {
      const actorId = currentUser.backendId ?? currentUser.id;
      const res = await fetch(`http://localhost:8080/api/reports/overdue?actorUserId=${actorId}`);
      if (res.ok) setOverdue(await res.json());
    };
    const loadUsers = async () => {
      const res = await fetch('http://localhost:8080/api/users');
      if (res.ok) setUsers(await res.json());
    };
    loadStats();
    loadOverdue();
    loadUsers();
  }, [currentUser, isAdmin, router]);

  const sendNotification = async (e) => {
    e.preventDefault();
    const res = await fetch('http://localhost:8080/api/notifications', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        actorUserId: currentUser.backendId ?? currentUser.id,
        userId: Number(notifyForm.userId),
        type: 'INFO',
        title: notifyForm.title,
        message: notifyForm.message,
      })
    });
    if (res.ok) {
      alert('Notification sent');
      setNotifyForm({ userId: '', title: '', message: '' });
    } else {
      alert('Send failed: ' + (await res.text()));
    }
  };

  return (
    <AppLayout>
      <h1>Admin Dashboard</h1>
      {stats ? (
        <div style={{ display: 'flex', gap: 16, marginTop: 12 }}>
          <div style={{ padding: 12, border: '1px solid #ddd', borderRadius: 6 }}>Active Loans<br/><strong>{stats.activeLoans}</strong></div>
          <div style={{ padding: 12, border: '1px solid #ddd', borderRadius: 6 }}>Total Loans<br/><strong>{stats.totalLoans}</strong></div>
          <div style={{ padding: 12, border: '1px solid #ddd', borderRadius: 6 }}>Total Reservations<br/><strong>{stats.totalReservations}</strong></div>
          <div style={{ padding: 12, border: '1px solid #ddd', borderRadius: 6 }}>Unpaid Penalties<br/><strong>{stats.unpaidPenalties}</strong></div>
        </div>
      ) : (
        <p>Loading stats...</p>
      )}

      <section style={{ marginTop: 24 }}>
        <h2>Overdue Loans</h2>
        <table>
          <thead>
            <tr>
              <th>Loan ID</th>
              <th>User ID</th>
              <th>Book ID</th>
              <th>Due Date</th>
            </tr>
          </thead>
          <tbody>
            {overdue.map(o => (
              <tr key={o.id}>
                <td>{o.id}</td>
                <td>{o.userId}</td>
                <td>{o.bookId}</td>
                <td>{o.dueDate}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </section>

      <section style={{ marginTop: 24 }}>
        <h2>Send Notification</h2>
        <form onSubmit={sendNotification} style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
          <select value={notifyForm.userId} onChange={e => setNotifyForm({ ...notifyForm, userId: e.target.value })} required>
            <option value="" disabled>Select user</option>
            {users.map(u => (
              <option key={u.id} value={u.id}>{u.name} ({u.email})</option>
            ))}
          </select>
          <input placeholder="Title" value={notifyForm.title} onChange={e => setNotifyForm({ ...notifyForm, title: e.target.value })} required />
          <input placeholder="Message" value={notifyForm.message} onChange={e => setNotifyForm({ ...notifyForm, message: e.target.value })} required style={{ width: 300 }} />
          <button type="submit">Send</button>
        </form>
      </section>

      <section style={{ marginTop: 24 }}>
        <h2>Quick Book Actions</h2>
        <p>Use the Books page for create/update/delete; this dashboard summarizes data and allows notifications.</p>
      </section>
    </AppLayout>
  );
}


