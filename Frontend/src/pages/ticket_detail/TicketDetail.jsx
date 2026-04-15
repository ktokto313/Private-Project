import React, { useState, useEffect } from 'react';
import './TicketDetail.css';

export default function TicketDetail({
  params,
}) {
  // Mock ticket state
  const [ticket, setTicket] = useState({
    id: ticketId || 1,
    title: 'Example Ticket Title',
    priority: { id: 1, name: 'High' },
    state: 'PROCESSING',
    type: { id: 1, name: 'Bug' },
    assignee: { id: 2, username: 'dev_user' },
    creator: { id: 1, username: 'creator_user' }
  });

  const [comment, setComment] = useState('');

  // Fallback map
  const priorities = [{id: 1, name: 'High'}, {id: 2, name: 'Low'}];
  const types = [{id: 1, name: 'Bug'}, {id: 2, name: 'Feature'}];
  const assignees = [{id: 2, username: 'dev_user'}, {id: 3, username: 'other_user'}];

  // Mock current user if not passed
  const user = currentUser || { id: 2, username: 'dev_user' };

  const isCreator = user.id === ticket.creator.id;
  const isAssignee = user.id === ticket.assignee?.id;
  const isResolved = ticket.state === 'RESOLVED';

  const updateTicketAPI = async (field, value) => {
    // API Call Stub
    console.log(`API update -> ${field}:`, value);
    setTicket(prev => ({ ...prev, [field]: value }));
  };

  const handlePostComment = () => {
    console.log("Post comment:", comment);
    setComment('');
  };

  const handleAddAsResolve = () => {
    console.log("Post comment & resolve:", comment);
    updateTicketAPI('state', 'RESOLVED');
    setComment('');
  };

  const handleMarkResolved = () => { // "If current user is assignee and ticket resolved" per prompt, handling logically 
    updateTicketAPI('state', 'RESOLVED');
  };

  const handleAccept = () => updateTicketAPI('state', 'DONE');
  const handleDeny = () => updateTicketAPI('state', 'PROCESSING');

  return (
    <div className="ticket-detail-container">
      <div className="split-layout">
        {/* Left Column */}
        <div className="left-panel">
          <div className="ticket-header">
            <h1 className="ticket-title">{ticket.title}</h1>
            <div className="ticket-badges">
              <span className={`badge priority-${ticket.priority.name.toLowerCase()}`}>{ticket.priority.name}</span>
              <span className={`badge state-${ticket.state.toLowerCase()}`}>{ticket.state}</span>
            </div>
          </div>

          <div className="comment-section">
            <textarea 
              value={comment}
              onChange={(e) => setComment(e.target.value)}
              placeholder="Add a comment..."
              className="comment-box"
            />
            <div className="comment-actions">
              <button className="btn primary-btn" onClick={handlePostComment}>Add</button>
              <button className="btn secondary-btn" onClick={handleAddAsResolve}>Add as Resolve</button>
            </div>
          </div>

          <div className="resolution-section">
            {isResolved && isCreator && (
              <div className="creator-actions">
                <button className="btn success-btn" onClick={handleAccept}>Accept</button>
                <button className="btn danger-btn" onClick={handleDeny}>Deny</button>
              </div>
            )}
            {/* Logic fallback: show if assignee. Added condition for flexibility. */}
            {isAssignee && !isResolved && (
              <div className="assignee-actions">
                <button className="btn warning-btn" onClick={handleMarkResolved}>Mark as Resolved</button>
              </div>
            )}
            {isAssignee && isResolved && (
               <div className="assignee-actions">
                 <button className="btn warning-btn" onClick={handleMarkResolved}>Mark as Resolved</button>
               </div>
            )}
          </div>
        </div>

        {/* Right Column */}
        <div className="right-panel">
          
          <div className="property-group">
            <div className="property-header">
              <label>Assignee</label>
              <a href="#" className="property-link" onClick={() => updateTicketAPI('assignee', user)}>Add</a>
            </div>
            <select 
              className="property-select"
              value={ticket.assignee?.id || ''}
              onChange={(e) => {
                const sel = assignees.find(a => a.id === parseInt(e.target.value));
                updateTicketAPI('assignee', sel || null);
              }}
            >
              <option value="">No one - assign self</option>
              {assignees.map(a => (
                <option key={a.id} value={a.id}>{a.username}</option>
              ))}
            </select>
          </div>

          <div className="property-group">
            <div className="property-header">
              <label>Priority</label>
              <a href="#" className="property-link">Change</a>
            </div>
            <select 
              className="property-select"
              value={ticket.priority.id}
              onChange={(e) => {
                const sel = priorities.find(p => p.id === parseInt(e.target.value));
                updateTicketAPI('priority', sel);
              }}
            >
               {priorities.map(p => (
                <option key={p.id} value={p.id}>{p.name}</option>
              ))}
            </select>
          </div>

          <div className="property-group">
            <div className="property-header">
              <label>Type</label>
              <a href="#" className="property-link">Add</a>
            </div>
            <select 
              className="property-select"
              value={ticket.type.id}
              onChange={(e) => {
                const sel = types.find(t => t.id === parseInt(e.target.value));
                updateTicketAPI('type', sel);
              }}
            >
              {types.map(t => (
                <option key={t.id} value={t.id}>{t.name}</option>
              ))}
            </select>
          </div>

        </div>
      </div>
    </div>
  );
}
