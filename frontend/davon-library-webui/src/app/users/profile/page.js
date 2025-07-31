'use client';

import { useEffect } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useUser } from '@/context/UserContext';
import styles from '../../page.module.css';

export default function ProfilePage() {
  const { currentUser, logoutUser } = useUser();
  const router = useRouter();

  useEffect(() => {
    // If no user is logged in, redirect to login page
    if (!currentUser) {
      router.push('/users/login');
    }
  }, [currentUser, router]);

  const handleLogout = () => {
    logoutUser();
    router.push('/users/login');
  };

  if (!currentUser) {
    // Render nothing or a loading spinner while redirecting
    return null;
  }

  return (
    <div>
      <nav>
        <Link href="/">Davon Library</Link>
        <ul>
          <li>
            <Link href="/users">Users</Link>
          </li>
          <li>
            <button onClick={handleLogout} style={{ background: 'none', border: 'none', cursor: 'pointer', color: '#0070f3', fontWeight: 'bold' }}>Logout</button>
          </li>
        </ul>
      </nav>
      <main className={styles.main}>
        <div className={styles.description} style={{ textAlign: 'left' }}>
          <h1>User Profile</h1>
          <div style={{ background: '#fff', padding: '2rem', borderRadius: '8px', boxShadow: '0 4px 8px rgba(0,0,0,0.1)' }}>
            <p><strong>Name:</strong> {currentUser.name}</p>
            <p><strong>Email:</strong> {currentUser.email}</p>
          </div>
        </div>
      </main>
    </div>
  );
}