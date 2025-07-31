'use client';

import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useUser } from '@/context/UserContext';
import AppLayout from '@/components/AppLayout';

export default function ProfilePage() {
  const { currentUser } = useUser();
  const router = useRouter();

  useEffect(() => {
    if (!currentUser) {
      router.push('/users/login');
    }
  }, [currentUser, router]);

  if (!currentUser) {
    return null; // Or a loading indicator
  }

  return (
    <AppLayout>
      <div style={{ paddingTop: '2rem' }}>
        <h1>User Profile</h1>
        <div style={{ background: '#fff', padding: '2rem', borderRadius: '8px', boxShadow: '0 4px 8px rgba(0,0,0,0.1)', marginTop: '1rem' }}>
          <p><strong>Name:</strong> {currentUser.name}</p>
          <p><strong>Email:</strong> {currentUser.email}</p>
        </div>
      </div>
    </AppLayout>
  );
}