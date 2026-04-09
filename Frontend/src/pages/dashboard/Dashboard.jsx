import React, { useState, useMemo, useRef, useEffect } from 'react';
import './Dashboard.css';

// --- PLACEHOLDER STUBS FOR FEATURES NOT IN SCOPE ---
const fetchTicketsAPI = async () => { /* API call stub */ };
const performAuth = async () => { /* Auth stub */ };
// const navigate = useNavigate(); /* Routing stub */
// ---------------------------------------------------

const MOCK_TICKETS = [
  {
    id: 'TKT-001',
    title: 'Cannot login to the system',
    status: 0, // Open
    priority: 'High',
    assignee: 'John Doe',
    createdDate: '2026-04-08'
  }
];

const STATUS_MAP = {
  0: 'Open',
  1: 'In Progress',
  2: 'Resolved',
  3: 'Closed'
};

const NewTicketForm = ({ onClose }) => (
  <div className="modal-overlay">
    <div className="modal-content">
      <h2>Create New Ticket (Stub)</h2>
      <p>This is a placeholder for the New Ticket Form.</p>
      <button onClick={onClose} className="btn primary-btn">Close Modal</button>
    </div>
  </div>
);

export default function Dashboard() {
  const [tickets, setTickets] = useState(MOCK_TICKETS);
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
              <tr key={ticket.id}>
                <td>{ticket.id}</td>
                <td>{ticket.title}</td>
                <td>
                  <span className={`status-badge status-${ticket.status}`}>
                    {STATUS_MAP[ticket.status]}
                  </span>
                </td>
                <td>{ticket.priority}</td>
                <td>{ticket.assignee}</td>
                <td>{ticket.createdDate}</td>
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
        <NewTicketForm onClose={() => setIsNewTicketModalOpen(false)} />
      )}
    </div>
  );
}
