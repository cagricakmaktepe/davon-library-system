'use client';

import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useUser } from '@/context/UserContext';

export default function AppLayout({ children }) {
  const { logoutUser } = useUser();
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
          <li>
            <Link href="/users">Users</Link>
          </li>
          <li>
            <Link href="/books">Books</Link>
          </li>
          <li>
            <Link href="/loans">Loans</Link>
          </li>
          <li>
            <Link href="/reservations">Reservations</Link>
          </li>
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
          {children}
        </div>
      </main>
    </div>
  );
}