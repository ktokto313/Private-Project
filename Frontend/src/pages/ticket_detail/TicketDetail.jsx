import React, { useState, useEffect } from 'react';
import './TicketDetail.css';
import { useAuth } from '../../context/AuthContext';
import { useParams } from 'react-router-dom';

export default function TicketDetail() {
    const { user } = useAuth();
    const { id } = useParams();
    const ticketID = id.substring(1);

    const [ticket, setTicket] = useState();
    const [comment, setComment] = useState('');
    const [priorities, setPriorities] = useState();
    const [ticketTypes, setTicketTypes] = useState();

    // Fallback map
    const assignees = [{ userID: 2, username: 'dev_user' }, { userID: 3, username: 'other_user' }];

    const isCreator = ticket ? user.ID === ticket.creator.ID : false;
    const isAssignee = ticket ? user.ID === ticket.assignee?.ID : false;
    const isResolved = ticket ? ticket.state === 'RESOLVED' : false;

    const fetchTicketDetail = async () => {
        var res = await fetch("/api/tickets/" + ticketID, {

        });
        if (!res.ok) return;
        res = await res.json();
        setTicket(res);
    }

    const fetchTicketTypes = async () => {
        const res = await fetch("/api/ticket-type", {
            method: "GET"
        })
        if (!res.ok) return;
        const resp = await res.json();
        setTicketTypes(resp);
    };

    const fetchPriorities = async () => {
        const res = await fetch("/api/priorities", {
            method: "GET"
        })
        if (!res.ok) return;
        const resp = await res.json();
        setPriorities(resp);
    };

    const updateTicketState = async (field, value) => {
        const data = new URLSearchParams();
        data.append("statusCode", value);
        const res = await fetch("/api/tickets/" + ticketID, {
            method: "PUT",
            body: data
        });
        if (res.ok) {
            console.log("Update success");
        }
    };

    const handlePostComment = () => {
        console.log("Post comment:", comment);
        setComment('');
    };

    const handleAddAsResolve = () => {
        console.log("Post comment & resolve:", comment);
        updateTicketState('state', 'RESOLVED');
        setComment('');
    };

    const handleMarkResolved = () => { // "If current user is assignee and ticket resolved" per prompt, handling logically 
        updateTicketState('state', 'RESOLVED');
    };

    const handleAccept = () => updateTicketState('state', 'DONE');
    const handleDeny = () => updateTicketState('state', 'PROCESSING');

    useEffect(() => {
        fetchTicketDetail();
        fetchTicketTypes();
        fetchPriorities();
    }, []);

    if (!ticket || !priorities || !ticketTypes) return;

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
                            <a href="#" className="property-link" onClick={() => updateTicketState('assignee', user)}>Add</a>
                        </div>
                        <select
                            className="property-select"
                            value={ticket.assignee?.userID || ''}
                            onChange={(e) => {
                                const sel = assignees.find(a => a.userID === parseInt(e.target.value));
                                updateTicketState('assignee', sel || null);
                            }}
                        >
                            <option value="">No one - assign self</option>
                            {assignees.map(a => (
                                <option key={a.userID} value={a.userID}>{a.username}</option>
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
                            value={ticket.priority.ID}
                            onChange={(e) => {
                                const sel = priorities.find(p => p.ID === parseInt(e.target.value));
                                updateTicketState('priority', sel);
                            }}
                        >
                            {priorities.map(p => (
                                <option key={p.ID} value={p.ID}>{p.name}</option>
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
                            value={ticket.ticketType.ID}
                            onChange={(e) => {
                                const sel = ticketTypes.find(t => t.ID === parseInt(e.target.value));
                                updateTicketState('type', sel);
                            }}
                        >
                            {ticketTypes.map(t => (
                                <option key={t.ID} value={t.ID}>{t.title}</option>
                            ))}
                        </select>
                    </div>

                </div>
            </div>
        </div>
    );
}
