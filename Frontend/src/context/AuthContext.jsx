import { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);

    const login = async (username, password) => {
        const res = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({"username":username, "password":password})
        });
        if (!res.ok) {
            throw new Error('Invalid credentials');
        }
        const user = await res.json();
        setUser(user);
    };

    const logout = async () => {
        const res = await fetch('/api/auth/logout', {
            method: 'POST',
            credentials: 'include',
            // headers: { 'Content-Type': 'application/json' },
        });
        if (!res.ok) throw new Error('Invalid credentials');
        setUser(null);
    };

    const getUserInfo = async () => {
        fetch('/api/users/me', {
            method: 'GET',
            credentials: 'include',
        })
            .then(function(response) {
                if (!response.ok) {
                    return;
                }
                return response.json();
            }).then(function(data) {
                setUser(data);
            }).catch();
    }

    useEffect(()=> {
        getUserInfo();
    }, []);

    return (
        <AuthContext.Provider value={{ user, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export const useAuth = () => useContext(AuthContext);