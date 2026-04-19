import React, { useState, useEffect, useMemo } from 'react';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, PieChart, Pie, Cell, Legend } from 'recharts';
import './Statistics.css';

const STATUS_COLORS = {
    DONE: '#4ade80', // green
    RESOLVED: '#60a5fa', // light blue
    PROCESSING: '#fde047', // beige/yellow
    CREATED: '#f87171' // red
};

const PIE_COLORS = ['#3b82f6', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6', '#ec4899', '#14b8a6', '#f97316'];

const formatDuration = (ms) => {
    if (isNaN(ms) || ms < 0) return "N/A";
    if (ms === 0) return "0m";
    const days = Math.floor(ms / (1000 * 60 * 60 * 24));
    const hours = Math.floor((ms % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
    const minutes = Math.floor((ms % (1000 * 60 * 60)) / (1000 * 60));
    let parts = [];
    if (days > 0) parts.push(`${days}d`);
    if (hours > 0) parts.push(`${hours}h`);
    if (minutes > 0) parts.push(`${minutes}m`);
    return parts.length > 0 ? parts.join(' ') : "< 1m";
};

const CustomRatioTooltip = ({ active, payload }) => {
    if (active && payload && payload.length) {
        const onTime = payload[0].payload.onTime;
        const overdue = payload[0].payload.overdue;
        const total = onTime + overdue;
        const onTimePct = total > 0 ? Math.round((onTime / total) * 100) : 0;
        const overduePct = total > 0 ? Math.round((overdue / total) * 100) : 0;
        return (
            <div className="custom-tooltip">
                <p className="success-text">On-time: {onTimePct}%</p>
                <p className="danger-text">Overdue: {overduePct}%</p>
            </div>
        );
    }
    return null;
};

export default function Statistics() {
    const [tickets, setTickets] = useState([]);
    const [priorities, setPriorities] = useState([]);
    const [ticketTypes, setTicketTypes] = useState([]);
    const [fromDate, setFromDate] = useState('');
    const [toDate, setToDate] = useState('');

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [ticketsRes, prioritiesRes, typesRes] = await Promise.all([
                    fetch('/api/tickets'),
                    fetch('/api/priorities'),
                    fetch('/api/ticket-type')
                ]);

                if (ticketsRes.ok) setTickets(await ticketsRes.json());
                if (prioritiesRes.ok) setPriorities(await prioritiesRes.json());
                if (typesRes.ok) setTicketTypes(await typesRes.json());
            } catch (err) {
                console.error("Failed to fetch data:", err);
            }
        };
        fetchData();
    }, []);

    const filteredTickets = useMemo(() => {
        return tickets.filter(t => {
            if (!t.timeCreated) return false;
            const d = new Date(t.timeCreated);
            if (fromDate && d < new Date(fromDate)) return false;
            if (toDate) {
                const td = new Date(toDate);
                td.setHours(23, 59, 59, 999);
                if (d > td) return false;
            }
            return true;
        });
    }, [tickets, fromDate, toDate]);

    // Section 1: Chart - Status Stacked Bar
    const statusData = useMemo(() => {
        const counts = { CREATED: 0, PROCESSING: 0, RESOLVED: 0, DONE: 0 };
        filteredTickets.forEach(t => {
            if (counts[t.state] !== undefined) counts[t.state]++;
            else counts[t.state] = 1;
        });
        return [{ name: 'Tickets', ...counts }];
    }, [filteredTickets]);

    // Section 2: List - Incident Type
    const incidentTypes = useMemo(() => {
        const types = {};
        filteredTickets.forEach(t => {
            const typeName = t.ticketType?.title || 'Unknown';
            types[typeName] = (types[typeName] || 0) + 1;
        });
        return Object.entries(types).map(([name, count]) => ({ name, count }));
    }, [filteredTickets]);

    // Section 3: Chart - Staff Pie
    const staffData = useMemo(() => {
        const staff = {};
        filteredTickets.forEach(t => {
            const name = t.assignee?.username || 'Unassigned';
            staff[name] = (staff[name] || 0) + 1;
        });
        return Object.entries(staff).map(([name, value]) => ({ name, value }));
    }, [filteredTickets]);

    // Section 4: Chart - SLA Ratio Stacked Bar
    const SLAData = useMemo(() => {
        let onTime = 0;
        let overdue = 0;

        filteredTickets.forEach(t => {
            const priority = priorities.find(p => p.ID === t.priority?.ID);
            if (!priority) {
                onTime++;
                return;
            }

            const getMs = (timeObj) => {
                if (!timeObj) return Infinity; // No SLA => Not overdue
                return ((timeObj.days || 0) * 24 * 60 * 60 + (timeObj.hours || 0) * 60 * 60 + (timeObj.minutes || 0) * 60) * 1000;
            };

            // const respondSLA = getMs(priority.timeToRespond);
            const finishSLA = getMs(priority.timeToFinish);

            const createdTime = new Date(t.timeCreated).getTime();
            // const procTime = t.timeProcessing ? new Date(t.timeProcessing).getTime() : new Date().getTime();
            const resTime = t.timeResolved ? new Date(t.timeResolved).getTime() : new Date().getTime();

            // const isRespondOverdue = (procTime - createdTime) > respondSLA;
            const isFinishOverdue = (resTime - createdTime) > finishSLA;

            if (/*isRespondOverdue || */isFinishOverdue) {
                overdue++;
            } else {
                onTime++;
            }
        });

        return [{ name: 'Ratio', onTime, overdue }];
    }, [filteredTickets, priorities]);

    // Section 5: Text - Average Handling Time
    const avgTimes = useMemo(() => {
        let totalMs = 0;
        let totalCount = 0;
        const tierMap = {};

        filteredTickets.forEach(t => {
            if (t.timeResolved && t.timeCreated) {
                const time = new Date(t.timeResolved).getTime() - new Date(t.timeCreated).getTime();
                if (time < 0) return; // Prevent negative time
                totalMs += time;
                totalCount++;

                const pName = t.priority?.name || 'Unknown';
                if (!tierMap[pName]) tierMap[pName] = { ms: 0, count: 0 };
                tierMap[pName].ms += time;
                tierMap[pName].count++;
            }
        });

        const overall = totalCount > 0 ? (totalMs / totalCount) : 0;
        const byTier = Object.entries(tierMap).map(([tier, data]) => ({
            tier,
            avg: data.count > 0 ? (data.ms / data.count) : 0
        }));

        return { overall, byTier };
    }, [filteredTickets]);

    // Section 6: Table - Backlog Tickets
    const backlogTickets = useMemo(() => {
        return filteredTickets.filter(t => t.state !== 'RESOLVED' && t.state !== 'DONE');
    }, [filteredTickets]);

    return (
        <div className="statistics-page slide-down">
            <div className="page-header">
                <h2>Statistics Dashboard</h2>
                <div className="filters">
                    <label>
                        Date From
                        <input type="date" value={fromDate} onChange={e => setFromDate(e.target.value)} />
                    </label>
                    <label>
                        Date To
                        <input type="date" value={toDate} onChange={e => setToDate(e.target.value)} />
                    </label>
                </div>
            </div>

            <div className="stats-grid">
                {/* Section 1 */}
                <div className="stats-card">
                    <h3>Tickets by Status</h3>
                    <div style={{ width: '100%', height: 100 }}>
                        <ResponsiveContainer>
                            <BarChart layout="vertical" data={statusData}>
                                <XAxis type="number" domain={[0, statusData[0].DONE + statusData[0].RESOLVED + statusData[0].PROCESSING + statusData[0].CREATED]} hide />
                                <YAxis type="category" dataKey="name" hide />
                                <Tooltip cursor={{ fill: 'transparent' }} contentStyle={{ borderRadius: '12px', border: 'none', boxShadow: '0 10px 15px -3px rgba(0,0,0,0.1)' }} />
                                <Bar dataKey="DONE" stackId="a" fill={STATUS_COLORS.DONE} name="Done" radius={[8, 0, 0, 8]} />
                                <Bar dataKey="RESOLVED" stackId="a" fill={STATUS_COLORS.RESOLVED} name="Resolved" />
                                <Bar dataKey="PROCESSING" stackId="a" fill={STATUS_COLORS.PROCESSING} name="Processing" />
                                <Bar dataKey="CREATED" stackId="a" fill={STATUS_COLORS.CREATED} name="Created" radius={[0, 8, 8, 0]} />
                            </BarChart>
                        </ResponsiveContainer>
                    </div>
                </div>

                {/* Section 2 */}
                <div className="stats-card" style={{ gridRow: 'span 2' }}>
                    <h3>Tickets by Incident Type</h3>
                    <ul className="incident-list">
                        {incidentTypes.map(t => (
                            <li key={t.name}>
                                <div className="incident-name">{t.name}</div>
                                <div className="incident-badge">{t.count}</div>
                            </li>
                        ))}
                        {incidentTypes.length === 0 && (
                            <li className="empty-state">No data available</li>
                        )}
                    </ul>
                </div>

                {/* Section 3 */}
                <div className="stats-card">
                    <h3>Tickets by Handler</h3>
                    <div style={{ width: '100%', height: 300 }}>
                        <ResponsiveContainer width="100%" height="100%">
                            <PieChart>
                                <Pie
                                    data={staffData}
                                    dataKey="value"
                                    nameKey="name"
                                    cx="50%"
                                    cy="50%"
                                    innerRadius={80}
                                    outerRadius={120}
                                    label
                                >
                                    {staffData.map((entry, index) => (
                                        <Cell key={`cell-${index}`} fill={PIE_COLORS[index % PIE_COLORS.length]} stroke="transparent" />
                                    ))}
                                </Pie>
                                <Tooltip contentStyle={{ borderRadius: '12px', border: 'none', boxShadow: '0 10px 15px -3px rgba(0,0,0,0.1)' }} />
                                <Legend wrapperStyle={{ paddingTop: '20px' }} />
                            </PieChart>
                        </ResponsiveContainer>
                    </div>
                </div>

                {/* Section 4 */}
                <div className="stats-card">
                    <h3>On-time vs Overdue</h3>
                    <div className="ratio-counts">
                        <span className="on-time-text">On-time: <strong>{SLAData[0].onTime}</strong></span>
                        <span className="overdue-text">Overdue: <strong>{SLAData[0].overdue}</strong></span>
                    </div>
                    <div style={{ width: '100%', height: 80 }}>
                        <ResponsiveContainer>
                            <BarChart layout="vertical" data={SLAData}>
                                <XAxis type="number" domain={[0, SLAData[0].onTime + SLAData[0].overdue]} hide />
                                <YAxis type="category" dataKey="name" hide />
                                <Tooltip content={<CustomRatioTooltip />} cursor={{ fill: 'transparent' }} />
                                <Bar dataKey="onTime" stackId="a" fill="#4ade80" radius={SLAData[0].overdue == 0 ? [8, 8, 8, 8] : [8, 0, 0, 8]} />
                                <Bar dataKey="overdue" stackId="a" fill="#f87171" radius={SLAData[0].onTime == 0 ? [8, 8, 8, 8] : [0, 8, 8, 0]} />
                            </BarChart>
                        </ResponsiveContainer>
                    </div>
                </div>

                {/* Section 5 */}
                <div className="stats-card">
                    <h3>Average Handling Time</h3>
                    <div className="avg-time-display">
                        <div className="overall-stat">
                            <span className="stat-value">{formatDuration(avgTimes.overall)}</span>
                            <div className="stat-label">Overall Average</div>
                        </div>
                        <ul className="tier-breakdown">
                            {avgTimes.byTier.map(t => (
                                <li key={t.tier}>
                                    <span className="tier-name">{t.tier}</span>
                                    <span className="tier-value">{formatDuration(t.avg)}</span>
                                </li>
                            ))}
                            {avgTimes.byTier.length === 0 && (
                                <li className="empty-state">No resolved tickets</li>
                            )}
                        </ul>
                    </div>
                </div>

            </div>

            {/* Section 6 */}
            <div className="backlog-section stats-card">
                <h3>Backlog Ticket List</h3>
                <div className="table-responsive">
                    <table className="backlog-table">
                        <thead>
                            <tr>
                                <th>Ticket ID</th>
                                <th>Title</th>
                                <th>Status</th>
                                <th>Assigned To</th>
                                <th>Created Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            {backlogTickets.map(t => (
                                <tr key={t.ID}>
                                    <td className="font-medium">#{t.ID}</td>
                                    <td>{t.title}</td>
                                    <td>
                                        <span className={`status-pill ${t.state.toLowerCase()}`}>
                                            {t.state}
                                        </span>
                                    </td>
                                    <td>{t.assignee?.username || 'Unassigned'}</td>
                                    <td>{new Date(t.timeCreated).toLocaleDateString()}</td>
                                </tr>
                            ))}
                            {backlogTickets.length === 0 && (
                                <tr>
                                    <td colSpan="5" className="empty-state" style={{ textAlign: 'center', padding: '3rem' }}>
                                        No backlogged tickets! 🎉
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}
