import { UserProvider } from '@/context/UserContext';
import './globals.css';

export const metadata = {
  title: 'Davon Library Web UI',
  description: 'Library Management System Web Interface',
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body>
        <UserProvider>{children}</UserProvider>
      </body>
    </html>
  );
}