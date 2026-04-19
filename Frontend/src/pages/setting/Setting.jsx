import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { ChangePasswordForm } from '../../components/ChangePasswordForm/ChangePasswordForm';
import './Setting.css';

export default function Setting() {
  const { user } = useAuth();
  const [isModalOpen, setIsModalOpen] = useState(false);

  const toggleModal = () => {
    setIsModalOpen(!isModalOpen);
  };

  if (!user) {
    return (
      <div className="setting-page">
        <main className="setting-main">
          <p>Loading user info...</p>
        </main>
      </div>
    );
  }

  // Assuming user object has department or department is an object with a name property
  const departmentName = user.department?.name || 'N/A';

  return (
    <div className="setting-page">
      <main className="setting-main">
        <div className="setting-content">
          <div className="user-info">
            <p>Username: {user.username}</p>
            <p>Department: {departmentName}</p>
          </div>

          <button className="change-password-btn" onClick={toggleModal}>
            Change password
          </button>
        </div>
      </main>

      {isModalOpen && (
        <ChangePasswordForm userID={user.userID} onClose={toggleModal} />
      )}
    </div>
  );
}
