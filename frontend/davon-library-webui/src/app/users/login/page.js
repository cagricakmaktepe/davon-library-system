'use client';

import { useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useUser } from '@/context/UserContext';
import styles from '../../page.module.css';

export default function LoginPage() {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });
  const [error, setError] = useState('');
  const { loginUser } = useUser();
  const router = useRouter();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setError('');
    const user = loginUser(formData.email, formData.password);
    if (user) {
      router.push('/users/profile');
    } else {
      setError('Invalid email or password.');
    }
  };

  return (
    <div>
      <nav>
        <Link href="/">Davon Library</Link>
        <ul>
          <li>
            <Link href="/users/register">Register</Link>
          </li>
          <li>
            <Link href="/users">Users</Link>
          </li>
        </ul>
      </nav>
      <main className={styles.main}>
        <div className={styles.description}>
          <h1>Login</h1>
          <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem', width: '300px' }}>
            <input
              type="email"
              name="email"
              placeholder="Email"
              value={formData.email}
              onChange={handleChange}
              required
              style={{ padding: '0.5rem' }}
            />
            <input
              type="password"
              name="password"
              placeholder="Password"
              value={formData.password}
              onChange={handleChange}
              required
              style={{ padding: '0.5rem' }}
            />
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <button type="submit" style={{ padding: '0.7rem', cursor: 'pointer' }}>Login</button>
          </form>
        </div>
      </main>
    </div>
  );
}