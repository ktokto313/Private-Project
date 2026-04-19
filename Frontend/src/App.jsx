// src/App.jsx
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Dashboard from './pages/dashboard/Dashboard';
import LoginPage from './pages/LoginPage'
import Profile from './pages/dashboard/Dashboard';
import './App.css';
import TicketDetail from './pages/ticket_detail/TicketDetail';
import AdminDashboard from './pages/admin_dashboard/AdminDashboard';
import Setting from './pages/setting/Setting';
import Statistics from './pages/statistic/Statistics';

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<LoginPage />} />
          <Route element={<ProtectedRoute />}>
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/profile" element={<Profile />} />
            <Route path="/ticket/:id" element={<TicketDetail />} />
            <Route path="/admin-dashboard" element={<AdminDashboard />} />
            <Route path="/setting" element={<Setting />} />
            <Route path="/statistics" element={<Statistics />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}