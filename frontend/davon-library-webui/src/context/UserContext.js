'use client';

import { createContext, useContext, useState, useEffect } from 'react';

const UserContext = createContext();

export const useUser = () => {
  return useContext(UserContext);
};

const initialUsers = [
  { id: 1, name: 'Alice', email: 'alice@example.com', password: 'password123' },
  { id: 2, name: 'Bob', email: 'bob@example.com', password: 'password123' },
];

export const UserProvider = ({ children }) => {
  const [users, setUsers] = useState(initialUsers);
  const [currentUser, setCurrentUser] = useState(null);

  // For now, we're not persisting to localStorage, but this is where you would load it.
  // useEffect(() => {
  //   const storedUsers = localStorage.getItem('users');
  //   if (storedUsers) {
  //     setUsers(JSON.parse(storedUsers));
  //   }
  // }, []);

  const addUser = (user) => {
    const newUser = { ...user, id: users.length + 1 };
    const updatedUsers = [...users, newUser];
    setUsers(updatedUsers);
    // localStorage.setItem('users', JSON.stringify(updatedUsers));
  };
  
  const loginUser = (email, password) => {
    const user = users.find(u => u.email === email && u.password === password);
    if (user) {
      setCurrentUser(user);
      return user;
    }
    return null;
  };
  
  const logoutUser = () => {
    setCurrentUser(null);
  };

  const value = {
    users,
    currentUser,
    addUser,
    loginUser,
    logoutUser,
  };

  return <UserContext.Provider value={value}>{children}</UserContext.Provider>;
};