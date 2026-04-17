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
    const [assignees, setAssignees] = useState([]);
    const [priorities, setPriorities] = useState();
    const [ticketTypes, setTicketTypes] = useState();

    const isCreator = ticket ? user.userID === ticket.creator.userID : false;
    const isAssignee = ticket ? user.userID === ticket.assignee.userID : false;
    const isResolved = ticket ? ticket.state === 'RESOLVED' : false;

    const fetchTicketDetail = async () => {
        var res = await fetch("/api/tickets/" + ticketID, {
            method: "GET"
        });
        if (!res.ok) return;
        res = await res.json();
        setTicket(res);
    }

    const fetchAssignee = async () => {
        if (user.role !== 'ADMIN') return;
        const res = await fetch("/api/admin/users?role=IT", {
            method: "GET"
        })
        if (!res.ok) return;
        const resp = await res.json();
        setAssignees(resp);
    };

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

    const updateTicketState = async (value) => {
        const res = await fetch((user.role === 'ADMIN' ? "/api/admin/tickets/" : "/api/tickets/") + ticketID + "?state=" + value, {
            method: "PUT",
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({})
        });
        if (res.ok) {
            console.log("Update success fetching data again!");
            fetchTicketDetail();
        }
    };

    const adminTicketUpdate = async (field, value) => {
        const tempTicket = {};
        switch (field) {
            case "assignee":
                tempTicket.assignee = { userID: value };
                break;
            case "priority":
                tempTicket.priority = { ID: value };
                break;
            case "ticketType":
                tempTicket.ticketType = { ID: value };
                break;
        }
        
        // const data = new URLSearchParams();
        console.log(JSON.stringify(tempTicket));
        // data.append("ticket", JSON.stringify(tempTicket));
        const res = await fetch("/api/admin/tickets/" + ticketID, {
            method: "PUT",
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(tempTicket)
        });
        if (res.ok) {
            console.log("Update success fetching data again!");
            fetchTicketDetail();
        }
    };

    const handlePostComment = async () => {
        const tempComment = {};
        tempComment.detail = comment;
        const data = new URLSearchParams();
        data.append("comment", tempComment);
        const res = await fetch("/api/tickets/" + ticketID + "/comments", {
            method: "POST",
            body: data
        });
        if (res.ok) {
            console.log("Posted comment:", comment);
            setComment('');
        }
    };

    const handleAddAsResolve = () => {
        handlePostComment();
        console.log("Post comment & resolve:", comment);
        updateTicketState('RESOLVED');
    };

    const handleMarkResolved = () => { // "If current user is assignee and ticket resolved" per prompt, handling logically 
        updateTicketState('RESOLVED');
    };

    const handleAccept = () => updateTicketState('DONE');
    const handleDeny = () => updateTicketState('PROCESSING');

    useEffect(() => {
        fetchTicketDetail();
        fetchAssignee();
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
                        {isResolved && (isCreator || user.role === 'ADMIN') && (
                            <div className="creator-actions">
                                <button className="btn success-btn" onClick={handleAccept}>Accept</button>
                                <button className="btn danger-btn" onClick={handleDeny}>Deny</button>
                            </div>
                        )}
                        {(isAssignee || user.role === 'ADMIN') && !isResolved && (
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
                            {user.role === 'ADMIN' && <a href="#" className="property-link" onClick={() => adminTicketUpdate('assignee', user)}>Add</a>}
                        </div>
                        <select
                            className="property-select"
                            value={ticket.assignee.userID} 
                            onChange={(e) => adminTicketUpdate('assignee', parseInt(e.target.value))}>
                            <option key={ticket.assignee.userID} value={ticket.assignee.userID}>{ticket.assignee.username}</option>
                            {user.role === 'ADMIN' && assignees.filter(a => a.userID !== ticket.assignee.userID).map(a => (
                                <option key={a.userID} value={a.userID}>{a.username}</option>
                            ))}
                        </select>
                    </div>

                    <div className="property-group">
                        <div className="property-header">
                            <label>Priority</label>
                            {user.role === 'ADMIN' && <a href="#" className="property-link">Change</a>}
                        </div>
                        <select
                            className="property-select"
                            value={ticket.priority.ID}
                            onChange={(e) => adminTicketUpdate('priority', parseInt(e.target.value))}
                        >
                            <option key={ticket.priority.ID} value={ticket.priority.ID}>{ticket.priority.name}</option>
                            {user.role === 'ADMIN' && priorities.filter(p => p.ID !== ticket.priority.ID).map(p => (
                                <option key={p.ID} value={p.ID}>{p.name}</option>
                            ))}
                        </select>
                    </div>

                    <div className="property-group">
                        <div className="property-header">
                            <label>Type</label>
                            {user.role === 'ADMIN' && <a href="#" className="property-link">Add</a>}
                        </div>
                        <select
                            className="property-select"
                            value={ticket.ticketType.ID}
                            onChange={(e) => adminTicketUpdate('ticketType', parseInt(e.target.value))}
                        >
                            <option key={ticket.ticketType.ID} value={ticket.ticketType.ID}>{ticket.ticketType.title}</option>
                            {user.role === 'ADMIN' && ticketTypes.filter(t => t.ID !== ticket.ticketType.ID).map(t => (
                                <option key={t.ID} value={t.ID}>{t.title}</option>
                            ))}
                        </select>
                    </div>

                </div>
            </div>
        </div>
    );
}
