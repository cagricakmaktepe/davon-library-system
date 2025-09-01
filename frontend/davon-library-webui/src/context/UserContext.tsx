'use client';

import { createContext, useContext, useState, useEffect, type ReactNode } from 'react';

type Role = 'ADMIN' | 'MEMBER';

type BaseUser = {
  id: number;
  name: string;
  email: string;
  password: string;
  role: Role;
};

type CurrentUser = BaseUser & { backendId: number | null | undefined };

type NewUserInput = {
  name: string;
  email: string;
  password: string;
  role?: Role;
};

type UserContextValue = {
  users: BaseUser[];
  currentUser: CurrentUser | null;
  isAdmin: () => boolean;
  isMember: () => boolean;
  addUser: (user: NewUserInput) => void;
  loginUser: (email: string, password: string) => Promise<CurrentUser | null>;
  logoutUser: () => void;
};

const UserContext = createContext<UserContextValue | undefined>(undefined);

export const useUser = (): UserContextValue => {
  const ctx = useContext(UserContext);
  if (!ctx) throw new Error('useUser must be used within UserProvider');
  return ctx;
};

const initialUsers: BaseUser[] = [
  { id: 1, name: 'Admin One', email: 'admin1@example.com', password: 'admin123', role: 'ADMIN' },
  { id: 2, name: 'Member One', email: 'member1@example.com', password: 'member123', role: 'MEMBER' },
];

export const UserProvider = ({ children }: { children: ReactNode }) => {
  const [users, setUsers] = useState<BaseUser[]>(initialUsers);
  const [currentUser, setCurrentUser] = useState<CurrentUser | null>(null);

  // For now, we're not persisting to localStorage, but this is where you would load it.
  // useEffect(() => {
  //   const storedUsers = localStorage.getItem('users');
  //   if (storedUsers) {
  //     setUsers(JSON.parse(storedUsers));
  //   }
  // }, []);

  const addUser = (user: NewUserInput) => {
    const newUser: BaseUser = { id: users.length + 1, role: user.role ?? 'MEMBER', name: user.name, email: user.email, password: user.password };
    const updatedUsers = [...users, newUser];
    setUsers(updatedUsers);
    // localStorage.setItem('users', JSON.stringify(updatedUsers));
  };
  
  const backendBase = 'http://localhost:8080';

  async function ensureBackendUser(user: BaseUser): Promise<number | null> {
    try {
      const listRes = await fetch(`${backendBase}/api/users`);
      if (listRes.ok) {
        const list = await listRes.json();
        const found = list.find(u => u.email === user.email);
        if (found) return found.id;
      }
      const createRes = await fetch(`${backendBase}/api/users`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: user.name, email: user.email, role: user.role || 'MEMBER' })
      });
      if (createRes.ok) {
        const created = await createRes.json();
        return created.id;
      }
    } catch (e) {
      // swallow and let caller handle missing mapping
    }
    return null;
  }

  const loginUser = async (email: string, password: string): Promise<CurrentUser | null> => {
    const user = users.find(u => u.email === email && u.password === password);
    if (!user) return null;
    const backendId = await ensureBackendUser(user);
    const enriched: CurrentUser = { ...user, backendId };
    setCurrentUser(enriched);
    return enriched;
  };
  
  const logoutUser = () => {
    setCurrentUser(null);
  };

  const value: UserContextValue = {
    users,
    currentUser,
    isAdmin: () => currentUser?.role === 'ADMIN',
    isMember: () => currentUser?.role === 'MEMBER',
    addUser,
    loginUser,
    logoutUser,
  };

  return <UserContext.Provider value={value}>{children}</UserContext.Provider>;
};