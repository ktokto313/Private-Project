import React, { useState, useMemo, useRef, useEffect } from 'react';
import './Dashboard.css';


// const navigate = useNavigate(); /* Routing stub */
// ---------------------------------------------------

import NewTicketForm from '../../components/NewTicketForm/NewTicketForm';

const STATUS_MAP = {
  0: 'CREATED',
  1: 'PROCESSING',
  2: 'RESOLVED',
  3: 'DONE'
};


export default function Dashboard() {
  const [tickets, setTickets] = useState([]);
  const [isFilterOpen, setIsFilterOpen] = useState(false);
  const [selectedFilters, setSelectedFilters] = useState([]);
  const [isNewTicketModalOpen, setIsNewTicketModalOpen] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const filterDropdownRef = useRef(null);

  const PAGE_SIZE = 20;

  useEffect(() => {
    function handleClickOutside(event) {
      if (filterDropdownRef.current && !filterDropdownRef.current.contains(event.target)) {
        setIsFilterOpen(false);
      }
    }
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [filterDropdownRef]);

  useEffect(()=> {
    fetchTicketsAPI();
  }, []);

  const handleFilterToggle = () => {
    setIsFilterOpen(!isFilterOpen);
  };

  const handleFilterChange = (statusKey) => {
    const key = parseInt(statusKey, 10);
    if (selectedFilters.includes(key)) {
      setSelectedFilters(selectedFilters.filter(f => f !== key));
    } else {
      setSelectedFilters([...selectedFilters, key]);
    }
    setCurrentPage(1);
  };

  const handleClearFilters = () => {
    setSelectedFilters([]);
    setCurrentPage(1);
  };

  const fetchTicketsAPI = async () => {
    var res = await fetch("/api/tickets", {
      method: "GET"
    })
    if (!res.ok) return;
    res = await res.json();
    setTickets(res);
  };

  const filteredTickets = useMemo(() => {
    if (selectedFilters.length === 0) return tickets;
    return tickets.filter(ticket => selectedFilters.includes(ticket.status));
  }, [tickets, selectedFilters]);

  const ticketCountsByStatus = useMemo(() => {
    const counts = { 0: 0, 1: 0, 2: 0, 3: 0 };
    tickets.forEach(ticket => {
      if (counts[ticket.status] !== undefined) {
        counts[ticket.status]++;
      }
    });
    return counts;
  }, [tickets]);

  const dateToDuration = (dateString) => {
    let ms = Date.parse(dateString);
    if (isNaN(ms)) return '';

    // If absolute date, get duration from now
    if (ms > 946684800000) {
      ms = Math.abs(Date.now() - ms);
    }

    const d = Math.floor(ms / 86400000);
    const h = Math.floor((ms % 86400000) / 3600000);
    const m = Math.floor((ms % 3600000) / 60000);
    const s = Math.floor((ms % 60000) / 1000);

    const parts = [];
    if (d > 0) parts.push(`${d}d`);
    if (h > 0) parts.push(`${h}h`);
    if (m > 0) parts.push(`${m}m`);
    if (s > 0) parts.push(`${s}s`);

    return parts.join(' ') || '0s';
  };

  const totalFilteredCount = filteredTickets.length;

  return (
    <div className="dashboard-container">
      <div className="dashboard-toolbar">
        <button className="btn outline-btn" onClick={() => setIsNewTicketModalOpen(true)}>
          Create new ticket
        </button>

        <div className="filter-wrapper" ref={filterDropdownRef}>
          <button className="btn outline-btn filter-btn" onClick={handleFilterToggle}>
            Filter
          </button>
          
          {isFilterOpen && (
            <div className="filter-dropdown">
              <div className="filter-dropdown-header">
                <span className="filter-dropdown-title">Filter by Status</span>
                <button className="btn text-btn clear-all-btn" onClick={handleClearFilters}>
                  Clear all
                </button>
              </div>
              <div className="filter-options">
                {[0, 1, 2, 3].map(statusKey => (
                  <label key={statusKey} className="filter-option">
                    <input 
                      type="checkbox" 
                      checked={selectedFilters.includes(statusKey)}
                      onChange={() => handleFilterChange(statusKey)}
                    />
                    <span className="status-label">{STATUS_MAP[statusKey]}</span>
                    <span className="badge">{ticketCountsByStatus[statusKey]}</span>
                  </label>
                ))}
              </div>
            </div>
          )}
        </div>
        
        <span className="filter-summary">
          Total ticket(s) in filter: {totalFilteredCount} tickets
        </span>
      </div>

      <div className="table-container">
        <table className="ticket-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Title</th>
              <th>Status</th>
              <th>Priority</th>
              <th>Assignee</th>
              <th>Created Date</th>
            </tr>
          </thead>
          <tbody>
            {filteredTickets.map(ticket => (
              <tr key={ticket.ID}>
                <td>{ticket.ID}</td>
                <td>{ticket.title}</td>
                <td>
                  <span className={`status-badge status-${ticket.status}`}>
                    {STATUS_MAP[ticket.status]}
                  </span>
                </td>
                <td>{ticket.priority.name}</td>
                <td>{ticket.assignee.username}</td>
                <td>{
                  new Date(Date.parse(ticket.timeCreated)).toDateString()
                }</td>
              </tr>
            ))}
            {filteredTickets.length === 0 && (
              <tr>
                <td colSpan="6" className="empty-state">No tickets found.</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* Pagination Stub */}
      <div className="pagination">
        <button className="page-item" disabled>&lt;</button>
        <button className="page-item active">1</button>
        <button className="page-item">2</button>
        <button className="page-item">3</button>
        <span className="page-dots">...</span>
        <button className="page-item">98</button>
        <button className="page-item">99</button>
        <button className="page-item">&gt;</button>
      </div>

      {isNewTicketModalOpen && (
        <NewTicketForm 
          onClose={() => setIsNewTicketModalOpen(false)} 
          onRefresh={() => console.log('Refresh tickets')} 
        />
      )}
    </div>
  );
}
