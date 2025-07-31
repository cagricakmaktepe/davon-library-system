'use client';

import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useUser } from '@/context/UserContext';
import AppLayout from '@/components/AppLayout';

export default function UsersPage() {
  const { users, currentUser } = useUser();
  const router = useRouter();

  useEffect(() => {
    if (!currentUser) {
      router.push('/users/login');
    }
  }, [currentUser, router]);
  
  if (!currentUser) {
    return null;
  }

  return (
    <AppLayout>
      <div style={{ paddingTop: '2rem' }}>
        <h1>User List</h1>
        <ul style={{ listStyle: 'none', padding: 0, width: '100%', maxWidth: '800px', marginTop: '1rem' }}>
          {users.map((user) => (
            <li key={user.id} style={{ background: '#fff', padding: '1rem', border: '1px solid #ddd', marginBottom: '1rem', borderRadius: '5px' }}>
              <p><strong>Name:</strong> {user.name}</p>
              <p><strong>Email:</strong> {user.email}</p>
            </li>
          ))}
        </ul>
      </div>
    </AppLayout>
  );
}