import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Navbar from './Navbar/Navbar';

export default function ProtectedRoute() {
  const { user } = useAuth();

  return (
    <>
      {user ?
        <>
          <Navbar />
          <Outlet />
        </>
        : <Navigate to="/" replace />}
    </>
  );
}