'use client';

import { useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useUser } from '@/context/UserContext';
import styles from '../../page.module.css';

export default function RegisterPage() {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
  });
  const { addUser } = useUser();
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
    addUser(formData);
    alert('Registration successful!');
    router.push('/users/login');
  };

  return (
    <div>
      <nav>
        <Link href="/">Davon Library</Link>
        <ul>
          <li>
            <Link href="/users/login">Login</Link>
          </li>
          <li>
            <Link href="/users">Users</Link>
          </li>
        </ul>
      </nav>
      <main className={styles.main}>
        <div className={styles.description}>
          <h1>Register</h1>
          <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem', width: '300px' }}>
            <input
              type="text"
              name="name"
              placeholder="Full Name"
              value={formData.name}
              onChange={handleChange}
              required
              style={{ padding: '0.5rem' }}
            />
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
            <button type="submit" style={{ padding: '0.7rem', cursor: 'pointer' }}>Register</button>
          </form>
        </div>
      </main>
    </div>
  );
}