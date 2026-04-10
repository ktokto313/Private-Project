import { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    // Validate token on app load
    useEffect(() => {
        // fetchCurrentUser(token)
        //     .then(setUser)
        //     .catch(() => { setToken(null); localStorage.removeItem('token'); })
        //     .finally(() => setLoading(false));
        try {
            getUserInfo();
        } catch (error) {
            
        } finally {
            setLoading(false);
        }
    }, []);

    const login = async (email, password) => {
        const res = await fetch('/auth/login', {
            method: 'POST',
            credentials: 'include',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password }),
        });
        if (!res.ok) throw new Error('Invalid credentials');
        const { user } = await res.json();
        setUser(user);
    };

    const logout = async () => {
        const res = await fetch('/auth/logout', {
            method: 'POST',
            credentials: 'include',
            // headers: { 'Content-Type': 'application/json' },
        });
        if (!res.ok) throw new Error('Invalid credentials');
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, login, logout, loading }}>
            {children}
        </AuthContext.Provider>
    );
}

async function getUserInfo() {
    const res = await fetch('/api/getUserInfo', {
        method: 'POST',
        credentials: 'include',
    });
    if (!res.ok) throw new Error('Invalid credentials');
    const { user } = await res.json();
    setUser(user);
}

export const useAuth = () => useContext(AuthContext);