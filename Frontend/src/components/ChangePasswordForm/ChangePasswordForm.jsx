import React, { useState } from 'react';
import './ChangePasswordForm.css';

export const ChangePasswordForm = ({ userID, onClose }) => {
  const [oldPassword, setOldPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [repeatPassword, setRepeatPassword] = useState('');

  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    // Validation: Check if fields are empty
    if (!oldPassword || !newPassword || !repeatPassword) {
      setError('All fields are required.');
      return;
    }

    // Validation: Check if new passwords match
    if (newPassword !== repeatPassword) {
      setError('New Password and Repeat New Password do not match.');
      return;
    }

    setIsLoading(true);

    try {
      const response = await fetch(`/api/users/${userID}/password`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          "password": oldPassword,
          "newPassword": newPassword
        })
      });

      if (response.status === 401) {
        setError('Wrong old password.');
        return;
      }

      if (!response.ok) {
        setError('An error occurred while changing the password.');
        return;
      }

      setSuccess('Password changed successfully!');

      // Reset form
      setOldPassword('');
      setNewPassword('');
      setRepeatPassword('');

      // Show success message and close the form
      setTimeout(() => {
        if (onClose) onClose();
      }, 1500);

    } catch (err) {
      setError('Failed to connect to the server.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-container">
        <h2 className="modal-title">-- Change Password --</h2>

        {error && (
          <div className="message-error">
            {error}
          </div>
        )}

        {success && (
          <div className="message-success">
            {success}
          </div>
        )}

        <form onSubmit={handleSubmit} noValidate>
          <div className="form-group">
            <label className="form-label">
              Old Password
            </label>
            <input
              type="password"
              className="form-input"
              value={oldPassword}
              onChange={(e) => setOldPassword(e.target.value)}
              placeholder="Enter your old password"
            />
          </div>

          <div className="form-group">
            <label className="form-label">
              New Password
            </label>
            <input
              type="password"
              className="form-input"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              placeholder="Enter your new password"
            />
          </div>

          <div className="form-group">
            <label className="form-label">
              Repeat New Password
            </label>
            <input
              type="password"
              className="form-input"
              value={repeatPassword}
              onChange={(e) => setRepeatPassword(e.target.value)}
              placeholder="Confirm your new password"
            />
          </div>

          <div className="form-actions">
            <button
              type="button"
              onClick={onClose}
              className="btn-cancel"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={isLoading}
              className="btn-submit"
            >
              {isLoading ? 'Submitting...' : 'Submit'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
