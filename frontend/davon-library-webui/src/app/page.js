import Link from 'next/link';
import styles from './page.module.css';

export default function HomePage() {
  return (
    <div>
      <nav>
        <Link href="/">Davon Library</Link>
        <ul>
          <li>
            <Link href="/users/register">Register</Link>
          </li>
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
          <h1>Welcome to the Davon Library</h1>
          <p>Manage users and books with ease.</p>
        </div>
      </main>
    </div>
  );
}