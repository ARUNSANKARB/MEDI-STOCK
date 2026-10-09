import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { AuthService } from '../services/api';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    try {
      const savedUser = localStorage.getItem('medistock_user') || localStorage.getItem('user');
      return savedUser ? JSON.parse(savedUser) : null;
    } catch {
      return null;
    }
  });

  const [token, setToken] = useState(() => {
    return localStorage.getItem('medistock_token') || localStorage.getItem('token') || null;
  });

  const [loading, setLoading] = useState(false);

  // Synchronize authentication state across multiple browser tabs automatically
  useEffect(() => {
    const handleStorageEvent = (event) => {
      if (
        event.key === 'medistock_token' ||
        event.key === 'token' ||
        event.key === 'medistock_user' ||
        event.key === 'user' ||
        event.key === null // Storage was cleared in another tab
      ) {
        const updatedToken = localStorage.getItem('medistock_token') || localStorage.getItem('token') || null;
        let updatedUser = null;
        try {
          const raw = localStorage.getItem('medistock_user') || localStorage.getItem('user');
          updatedUser = raw ? JSON.parse(raw) : null;
        } catch {
          updatedUser = null;
        }

        setToken(updatedToken);
        setUser(updatedUser);
      }
    };

    window.addEventListener('storage', handleStorageEvent);
    return () => window.removeEventListener('storage', handleStorageEvent);
  }, []);

  const login = async (email, password) => {
    setLoading(true);
    try {
      const data = await AuthService.login(email, password);
      setUser(data.user);
      setToken(data.token);
      localStorage.setItem('medistock_token', data.token);
      localStorage.setItem('token', data.token);
      localStorage.setItem('medistock_user', JSON.stringify(data.user));
      localStorage.setItem('user', JSON.stringify(data.user));
      return { success: true };
    } catch (error) {
      return { 
        success: false, 
        message: error.response?.data?.message || 'Invalid credentials. Try admin@medistock.com / admin123' 
      };
    } finally {
      setLoading(false);
    }
  };

  const updateProfile = async (updatedData) => {
    setLoading(true);
    try {
      const res = await AuthService.updateProfile(updatedData);
      const newUser = res.user || { ...user, ...updatedData };
      setUser(newUser);
      localStorage.setItem('medistock_user', JSON.stringify(newUser));
      localStorage.setItem('user', JSON.stringify(newUser));
      return { success: true, user: newUser, message: res.message || 'Profile updated successfully' };
    } catch (error) {
      return {
        success: false,
        message: error.message || 'Failed to update profile'
      };
    } finally {
      setLoading(false);
    }
  };

  const logout = useCallback(() => {
    setUser(null);
    setToken(null);
    localStorage.removeItem('medistock_token');
    localStorage.removeItem('token');
    localStorage.removeItem('medistock_user');
    localStorage.removeItem('user');
  }, []);

  return (
    <AuthContext.Provider value={{ user, token, isAuthenticated: !!token, login, logout, updateProfile, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);

export default AuthContext;
