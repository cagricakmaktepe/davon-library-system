'use client';

import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useUser } from '@/context/UserContext';

export default function AppLayout({ children }) {
  const { logoutUser, currentUser, isAdmin, isMember } = useUser();
  const router = useRouter();

  const handleLogout = () => {
    logoutUser();
    router.push('/users/login');
  };

  return (
    <div>
      <nav>
        <Link href="/users/profile"><strong>Davon Library</strong></Link>
        <ul>
          {isAdmin() && (
            <li>
              <Link href="/users">Users</Link>
            </li>
          )}
          <li>
            <Link href="/books">Books</Link>
          </li>
          <li>
            <Link href="/loans">Loans</Link>
          </li>
          <li>
            <Link href="/reservations">Reservations</Link>
          </li>
          {isMember() && (
            <li>
              <Link href="/member">Member</Link>
            </li>
          )}
          {isAdmin() && (
            <li>
              <Link href="/admin">Admin</Link>
            </li>
          )}
          {isAdmin() && (
            <li>
              <span style={{ color: '#0a0' }}>[Admin]</span>
            </li>
          )}
          <li>
            <Link href="/users/profile">Profile</Link>
          </li>
          <li>
            <button onClick={handleLogout} style={{ background: 'none', border: 'none', cursor: 'pointer', color: '#0070f3', fontWeight: 'normal', padding: 0, fontSize: '1rem' }}>Logout</button>
          </li>
        </ul>
      </nav>
      <main>
        <div className="container">
          {currentUser && (
            <div style={{ marginBottom: 12, fontSize: '0.9rem', color: '#555' }}>
              Signed in as: <strong>{currentUser.name}</strong> ({currentUser.role})
            </div>
          )}
          {children}
        </div>
      </main>
    </div>
  );
}