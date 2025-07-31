'use client';

import Link from 'next/link';
import { useUser } from '@/context/UserContext';
import styles from '../page.module.css';

export default function UsersPage() {
  const { users, currentUser } = useUser();

  return (
    <div>
      <nav>
        <Link href="/">Davon Library</Link>
        <ul>
          {currentUser ? (
            <>
              <li>
                <Link href="/users/profile">Profile</Link>
              </li>
            </>
          ) : (
            <>
              <li>
                <Link href="/users/register">Register</Link>
              </li>
              <li>
                <Link href="/users/login">Login</Link>
              </li>
            </>
          )}
        </ul>
      </nav>
      <main className={styles.main}>
        <div className={styles.description} style={{ textAlign: 'left', width: '100%' }}>
          <h1>User List</h1>
          <ul style={{ listStyle: 'none', padding: 0, width: '100%', maxWidth: '600px' }}>
            {users.map((user) => (
              <li key={user.id} style={{ background: '#fff', padding: '1rem', border: '1px solid #ddd', marginBottom: '1rem', borderRadius: '5px' }}>
                <p><strong>Name:</strong> {user.name}</p>
                <p><strong>Email:</strong> {user.email}</p>
              </li>
            ))}
          </ul>
        </div>
      </main>
    </div>
  );
}