// src/components/Navbar.jsx
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

export default function Navbar() {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    return (
        <nav>
            <span>Hello, {user.name}</span>
            <button onClick={() => { try {logout()} catch (error) {}; navigate('/login'); }}>
                Sign out
            </button>
        </nav>
    );
}