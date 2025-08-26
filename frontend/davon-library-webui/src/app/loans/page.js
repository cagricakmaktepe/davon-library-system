'use client';

import { useEffect, useState } from 'react';
import AppLayout from '@/components/AppLayout';
import { useUser } from '@/context/UserContext';

export default function LoansPage() {
  const { currentUser } = useUser();
  const [loans, setLoans] = useState([]);

  const loadLoans = async () => {
    if (!currentUser) return;
    const res = await fetch(`http://localhost:8080/api/loans/user/${currentUser.id}`);
    const data = await res.json();
    setLoans(data);
  };

  useEffect(() => {
    loadLoans();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentUser?.id]);

  const returnLoan = async (loanId) => {
    const res = await fetch(`http://localhost:8080/api/loans/${loanId}/return`, { method: 'POST' });
    if (res.ok) {
      await loadLoans();
      alert('Returned');
    } else {
      alert('Failed to return');
    }
  };

  return (
    <AppLayout>
      <h2>My Loans</h2>
      {!currentUser ? (
        <p>Please login to view your loans.</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Loan ID</th>
              <th>Book ID</th>
              <th>Checkout</th>
              <th>Due</th>
              <th>Return</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {loans.map(l => (
              <tr key={l.id}>
                <td>{l.id}</td>
                <td>{l.bookId}</td>
                <td>{l.checkoutDate}</td>
                <td>{l.dueDate}</td>
                <td>{l.returnDate || '-'}</td>
                <td>
                  <button onClick={() => returnLoan(l.id)} disabled={!!l.returnDate}>Return</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </AppLayout>
  );
}


