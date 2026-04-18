import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import './Navbar.css'

export default function Navbar() {
  const [isOpen, setIsOpen] = useState(false);
  const navigate = useNavigate();

  const { user, logout } = useAuth();

  const handleLogout = async () => {
    await logout()
      .catch(() => { return; });
    navigate('/');
  };

  return (
    <>
      <nav className="navbar-container">
        <div className="nav-links">
          <Link to="/dashboard" className="nav-item">Dashboard</Link>
          {user.role === 'ADMIN' && (
            <>
              <Link to="/admin-dashboard" className="nav-item">Admin Dashboard</Link>
              <Link to="/statistics" className="nav-item">Statistic</Link>
            </>
          )}
        </div>

        <div className="nav-user">
          <button
            className="user-btn"
            onClick={() => setIsOpen(!isOpen)}
            onBlur={() => setTimeout(() => setIsOpen(false), 200)}
          >
            Hi, {user.username}
            <span style={{ fontSize: '0.8em', transition: 'transform 0.3s', transform: isOpen ? 'rotate(180deg)' : 'rotate(0)' }}>
              ▼
            </span>
          </button>

          {isOpen && (
            <div className="dropdown-menu">
              <Link to="/settings" className="drop-item">Setting</Link>
              <button onClick={handleLogout} className="drop-item logout">
                Log Out
              </button>
            </div>
          )}
        </div>
      </nav>
    </>
  );
}
