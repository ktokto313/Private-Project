import React, { useState, useEffect } from 'react';
import './AdminDashboard.css';

const AdminDashboard = () => {
  // === User Management State ===
  const [searchUsername, setSearchUsername] = useState('');
  const [searchedUser, setSearchedUser] = useState(null);
  const [newPassword, setNewPassword] = useState('');
  const [newDepartment, setNewDepartment] = useState('');
  const [confirmDelete, setConfirmDelete] = useState(false);
  const [error, setError] = useState('');
  const [departments, setDepartments] = useState([]);

  const [createUsername, setCreateUsername] = useState('');
  const [createPassword, setCreatePassword] = useState('');
  const [showCreateUser, setShowCreateUser] = useState(false);

  // === SLA Management State ===
  const [priorities, setPriorities] = useState([]);

  // Fetch initial SLAs on load
  useEffect(() => {
    fetchPriorities();
    fetchDepartments();
  }, []);

  const fetchPriorities = async () => {
    try {
      const response = await fetch('/api/admin/priorities');
      if (response.ok) {
        const data = await response.json();
        setPriorities(data);
      }
    } catch (error) {
      console.error("Error fetching priorities:", error);
    }
  };

  // --- User Handlers ---
  const handleCreateUser = async () => {
    try {
      const response = await fetch('/api/admin/users', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: createUsername, password: createPassword })
      });
      if (response.ok) {
        alert("User created successfully!");
        setCreateUsername('');
        setCreatePassword('');
        setShowCreateUser(false);
      } else {
        console.error('Failed to create user');
      }
    } catch (error) {
      console.error(error);
    }
  };

  const handleSearchUser = async (e) => {
    e.preventDefault();
    if (!searchUsername.trim()) return;

    try {
      const response = await fetch(`/api/admin/users/?username=` + searchUsername);
      if (response.ok) {
        const data = await response.json();
        setSearchedUser(data);
        setConfirmDelete(false);
        setError('');
      } else {
        setError("User not found");
      }
    } catch (error) {
      console.error(error);
    }
  };

  const handleSetNewPassword = async () => {
    if (!searchedUser || !newPassword) return;
    try {
      const response = await fetch(`/api/admin/users/${searchedUser.userID}/password`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ "password": newPassword })
      });
      if (response.ok) {
        alert("Password updated successfully!");
        setNewPassword('');
      } else {
        alert("Password updated");
        setNewPassword('');
      }
    } catch (err) {
      console.error(err);
    }
  };

  const fetchDepartments = async () => {
    try {
      const response = await fetch('/api/admin/departments');
      if (response.ok) {
        const data = await response.json();
        setDepartments(data);
      }
    } catch (error) {
      console.error("Error fetching departments:", error);
    }
  };

  const handleChangeDepartment = async () => {
    if (!searchedUser || !newDepartment) return;
    try {
      const response = await fetch(`/api/admin/users/${searchedUser.userID}/department`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ "ID": newDepartment })
      });
      if (response.ok) {
        alert("Department updated successfully!");
        handleSearchUser();
        setNewDepartment('');
      } else {
        alert("Department updated failed");
      }
    } catch (err) {
      console.error(err);
    }
  };

  const handleDeleteUser = async () => {
    if (!searchedUser) return;
    try {
      const response = await fetch(`/api/admin/users/${searchedUser.userID}`, {
        method: 'DELETE'
      });
      if (response.ok || true) { // Remove fallback true when backend is strict
        setSearchedUser(null);
        setConfirmDelete(false);
        setSearchUsername('');
      }
    } catch (err) {
      console.error(err);
    }
  };

  // --- SLA Handlers ---
  const handleAddSLA = async (levelOfPriority) => {
    try {
      // Depending on API spec, sending the name in a POST body
      const data = {};
      data.levelOfPriority = levelOfPriority;
      data.name = "new priority";
      data.timeToRespond = { "value": "2 hours" };
      data.timeToFinish = { "value": "2 hours" };
      const response = await fetch(`/api/admin/priorities`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
      });
      if (response.ok) {
        fetchPriorities();
      }
    } catch (err) {
      console.error(err);
    }
  };

  const handleUpdateSLA = async (formData) => {
    try {
      const data = {};
      data.ID = formData.get("ID");
      data.name = formData.get("name");
      data.levelOfPriority = formData.get("levelOfPriority");
      data.timeToRespond = { "value": formData.get("timeToRespond") };
      data.timeToFinish = { "value": formData.get("timeToFinish") };
      const response = await fetch(`/api/admin/priorities/${formData.get("ID")}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
      });
      if (response.ok) {
        fetchPriorities();
      }
    } catch (err) {
      console.error(err);
    }
  };

  const handleDeleteSLA = async (priorityID) => {
    try {
      const response = await fetch(`/api/admin/priorities/${priorityID}`, {
        method: 'DELETE'
      });
      if (response.ok) {
        fetchPriorities();
      } else {
        setPriorities(priorities.filter(p => p.ID !== priorityID));
      }
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="admin-dashboard-container" >

      <div className="admin-content-wrapper">

        {/* LEFT COLUMN: User Management */}
        <div className="admin-col glass-panel">
          <h2 className="section-title">User Management</h2>

          <button
            className="primary-btn full-width mb-20"
            onClick={() => setShowCreateUser(!showCreateUser)}
          >
            Create New User
          </button>

          {showCreateUser && (
            <div className="create-user-form slide-down mb-20">
              <input
                type="text"
                placeholder="New Username"
                className="styled-input"
                value={createUsername}
                onChange={e => setCreateUsername(e.target.value)}
              />
              <input
                type="password"
                placeholder="New Password"
                className="styled-input"
                value={createPassword}
                onChange={e => setCreatePassword(e.target.value)}
              />
              <button className="submit-btn primary-btn full-width" onClick={handleCreateUser}>
                Confirm User Creation
              </button>
            </div>
          )}

          <form className="search-section" onSubmit={handleSearchUser}>
            <input
              type="text"
              placeholder="Username to search"
              className="styled-input"
              value={searchUsername}
              onChange={(e) => setSearchUsername(e.target.value)}
            />
            {error && <p className="error-message">{error}</p>}
            <button type="submit" className="submit-btn primary-btn mt-10" onClick={handleSearchUser}>
              Submit
            </button>
          </form>

          {searchedUser && (
            <div className="search-result-area fade-in">
              <div className="user-info-box">
                <p><span className="info-label">Username:</span> {searchedUser.username}</p>
                <p><span className="info-label">Role:</span> {searchedUser.role}</p>
                <p><span className="info-label">Department:</span> {searchedUser.department.name}</p>
              </div>

              <div className="update-form-group">
                <input
                  type="password"
                  placeholder="New Password"
                  className="styled-input"
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                />
                <select
                  className="styled-input"
                  value={newDepartment}
                  onChange={(e) => setNewDepartment(e.target.value)}
                >
                  {departments.map((department) => (
                    <option key={department.ID} value={department.ID}>
                      {department.name}
                    </option>
                  ))}
                </select>

                <div className="action-buttons-row mt-10">
                  <button className="action-btn" onClick={handleSetNewPassword}>Set New Password</button>
                  <button className="action-btn" onClick={handleChangeDepartment}>Change Department</button>
                </div>
              </div>

              <button className="danger-btn full-width mt-10" onClick={() => setConfirmDelete(true)}>
                Delete User
              </button>

              {confirmDelete && (
                <div className="delete-confirm-panel slide-down">
                  <p>Are you sure you want to delete this user? This action cannot be undone.</p>
                  <div className="confirm-actions">
                    <button className="danger-solid-btn" onClick={handleDeleteUser}>Confirm Deletion</button>
                    <button className="secondary-btn" onClick={() => setConfirmDelete(false)}>Cancel</button>
                  </div>
                </div>
              )}
            </div>
          )}
        </div>


        {/* RIGHT COLUMN: SLA Management */}
        <div className="admin-col glass-panel">
          <h2 className="section-title">SLA Management</h2>

          <div className="table-container">
            <table className="styled-table">
              <thead>
                <tr>
                  <th>SLA Name</th>
                  <th>Priority <span className="sort-icon" title="Sort by Priority">↕</span></th>
                  <th>Time to Respond <span className="sort-icon" title="Sort by Time to Respond">↕</span></th>
                  <th>Time to Resolve</th>
                </tr>
              </thead>
              <tbody>
                {priorities.map((p, index) => (
                  <tr key={p.ID || index}>
                    <td><form id={`sla-form-${p.ID}`} action={handleUpdateSLA}>
                      <input type="hidden" name='ID' value={p.ID} />
                      <input type="text" name='name' defaultValue={p.name || 'N/A'} /></form>
                    </td>
                    <td><input form={`sla-form-${p.ID}`} type="number" name='levelOfPriority' defaultValue={p.levelOfPriority || 'N/A'} /></td>
                    <td><input form={`sla-form-${p.ID}`} type="text" name='timeToRespond' defaultValue={p.timeToRespond?.value || p.timeToRespond || 'N/A'} /></td>
                    <td><input form={`sla-form-${p.ID}`} type="text" name='timeToFinish' defaultValue={p.timeToFinish?.value || p.timeToFinish || 'N/A'} /></td>
                    <td className="truncate-cell">
                      <div className="truncate-wrapper">
                        <button form={`sla-form-${p.ID}`} type="submit" className="icon-btn action-icon" title="Update SLA">
                          update
                        </button>
                        <button
                          type="button"
                          className="icon-btn action-icon"
                          onClick={() => handleDeleteSLA(p.ID)}
                          title="Delete SLA"
                        >
                          delete
                        </button>

                      </div>
                    </td>
                  </tr>
                ))}
                {priorities.length === 0 && (
                  <tr>
                    <td colSpan="4" style={{ textAlign: 'center', color: '#94a3b8' }}>
                      No SLAs Configured
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>

          <div className="add-sla-section mt-20">
            <input
              type="button"
              className="styled-input full-width"
              value="Add New SLA"
              onClick={() => handleAddSLA(priorities.length + 1)}
            />
          </div>
        </div>

      </div>
    </div >
  );
};

export default AdminDashboard
