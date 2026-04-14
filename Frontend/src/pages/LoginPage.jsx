// src/pages/LoginPage.jsx
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function LoginPage() {
  const { login, user } = useAuth();
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [status, setStatus] = useState('idle'); // idle | loading | error

  const handleSubmit = async (e) => {
    e.preventDefault();
    setStatus('loading');
    try {
      await login(username, password);
      navigate('/dashboard');
    } catch {
      setStatus('error');
    }
  };

  useEffect(()=> {
    if (user)
      navigate('/dashboard');
  }, [user]);

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        placeholder="Username"
        required
      />
      <input
        type="password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        placeholder="Password"
        required
      />

      {status === 'error' && <p style={{ color: 'red' }}>Invalid credentials</p>}

      <button type="submit" disabled={status === 'loading'}>
        {status === 'loading' ? 'Signing in...' : 'Sign in'}
      </button>
    </form>
  );
}