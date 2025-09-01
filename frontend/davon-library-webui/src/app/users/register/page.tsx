'use client';

import { useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useUser } from '@/context/UserContext';
import '@/app/globals.css'; // Using globals for simplicity

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
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
      <div style={{ width: '320px', padding: '2rem', boxShadow: '0 4px 12px rgba(0,0,0,0.1)', borderRadius: '8px', background: '#fff' }}>
        <h1 style={{ textAlign: 'center', marginBottom: '1.5rem' }}>Create Account</h1>
        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          <input
            type="text"
            name="name"
            placeholder="Full Name"
            value={formData.name}
            onChange={handleChange}
            required
            style={{ padding: '0.75rem', border: '1px solid #ddd', borderRadius: '4px' }}
          />
          <input
            type="email"
            name="email"
            placeholder="Email"
            value={formData.email}
            onChange={handleChange}
            required
            style={{ padding: '0.75rem', border: '1px solid #ddd', borderRadius: '4px' }}
          />
          <input
            type="password"
            name="password"
            placeholder="Password"
            value={formData.password}
            onChange={handleChange}
            required
            style={{ padding: '0.75rem', border: '1px solid #ddd', borderRadius: '4px' }}
          />
          <button type="submit" style={{ padding: '0.75rem', cursor: 'pointer', background: '#0070f3', color: '#fff', border: 'none', borderRadius: '4px', fontWeight: 'bold' }}>Register</button>
        </form>
        <p style={{ textAlign: 'center', marginTop: '1.5rem' }}>
          Already have an account? <Link href="/users/login">Login</Link>
        </p>
      </div>
    </div>
  );
}