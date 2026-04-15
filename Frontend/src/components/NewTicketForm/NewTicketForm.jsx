import React, { useState, useEffect } from 'react';
import './NewTicketForm.css';

const fetchTicketTypesAPI = async () => {
  fetch("/api/ticket-type", {
    method: "GET"
  })
    .then((res)=> {
      return res.json();
    })
};

const submitTicketAPI = async (ticketData) => {
  fetch("/api/tickets", {
    method: "GET"
  })
    .then((res)=> {
      console.log('Ticket submitted successfully:', ticketData);
      if (res.ok) return { success: true };
    })
};
// ---------------------------

const NewTicketForm = ({ onClose, onRefresh }) => {
  const [formData, setFormData] = useState({
    title: '',
    detail: '',
    priority: '',
    problemType: '',
    attachments: []
  });
  
  const [ticketTypes, setTicketTypes] = useState([]);
  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    // Fetch problem types on mount
    const fetchOptions = async () => {
      try {
        const types = await fetchTicketTypesAPI();
        setTicketTypes(types);
      } catch (error) {
        console.error("Failed to fetch problem types", error);
      }
    };
    fetchOptions();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    // Clear error for this field
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: null }));
    }
  };

  const handleFileChange = (e) => {
    const files = Array.from(e.target.files);
    setFormData(prev => ({
      ...prev,
      attachments: [...prev.attachments, ...files]
    }));
  };

  const removeAttachment = (index) => {
    setFormData(prev => {
      const newAttachments = [...prev.attachments];
      newAttachments.splice(index, 1);
      return { ...prev, attachments: newAttachments };
    });
  };

  const validateForm = () => {
    const newErrors = {};
    if (!formData.title.trim()) newErrors.title = 'Title is required';
    if (!formData.detail.trim()) newErrors.detail = 'Detail is required';
    if (!formData.priority) newErrors.priority = 'Priority is required';
    if (!formData.problemType) newErrors.problemType = 'Request/Problem type is required';
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateForm()) return;

    setIsSubmitting(true);
    try {
      // Create FormData to simulate sending files
      const submissionData = new FormData();
      submissionData.append('title', formData.title);
      submissionData.append('detail', formData.detail);
      submissionData.append('priority', formData.priority);
      submissionData.append('problemType', formData.problemType);
      formData.attachments.forEach(file => {
        submissionData.append('attachments', file);
      });

      // Submit
      await submitTicketAPI(submissionData);
      
      // On success
      if (onRefresh) onRefresh();
      onClose();
    } catch (error) {
      console.error("Error submitting ticket", error);
      setErrors({ submit: 'Failed to submit ticket. Please try again.' });
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="ntf-modal-overlay">
      <div className="ntf-modal-content">
        <form onSubmit={handleSubmit} className="ntf-form">
          <div className="ntf-form-group">
            <label htmlFor="title">Title</label>
            <input
              type="text"
              id="title"
              name="title"
              value={formData.title}
              onChange={handleInputChange}
              className={errors.title ? 'ntf-error-input' : ''}
              disabled={isSubmitting}
            />
            {errors.title && <span className="ntf-error-text">{errors.title}</span>}
          </div>

          <div className="ntf-form-group">
            <label htmlFor="detail">Detail</label>
            <textarea
              id="detail"
              name="detail"
              value={formData.detail}
              onChange={handleInputChange}
              className={`ntf-detail-textarea ${errors.detail ? 'ntf-error-input' : ''}`}
              placeholder="textbox must expand or have a scroll bar"
              disabled={isSubmitting}
            ></textarea>
            {errors.detail && <span className="ntf-error-text">{errors.detail}</span>}
          </div>

          <div className="ntf-row">
            <div className="ntf-form-group ntf-half-width">
              <label htmlFor="priority">Priority</label>
              <select
                id="priority"
                name="priority"
                value={formData.priority}
                onChange={handleInputChange}
                className={errors.priority ? 'ntf-error-input' : ''}
                disabled={isSubmitting}
              >
                <option value="" disabled></option>
                <option value="Low">Low</option>
                <option value="Medium">Medium</option>
                <option value="High">High</option>
              </select>
              {errors.priority && <span className="ntf-error-text">{errors.priority}</span>}
            </div>

            <div className="ntf-form-group ntf-half-width">
              <label htmlFor="problemType">Request/Problem type</label>
              <select
                id="problemType"
                name="problemType"
                value={formData.problemType}
                onChange={handleInputChange}
                className={errors.problemType ? 'ntf-error-input' : ''}
                disabled={isSubmitting}
              >
                <option value="" disabled></option>
                {ticketTypes.map(type => (
                  <option key={type.id} value={type.id}>{type.name}</option>
                ))}
              </select>
              {errors.problemType && <span className="ntf-error-text">{errors.problemType}</span>}
            </div>
          </div>

          <div className="ntf-form-group">
            <label htmlFor="attachments">Attachment(s)</label>
            <div className="ntf-file-upload-box">
              <input
                type="file"
                id="attachments"
                name="attachments"
                multiple
                onChange={handleFileChange}
                disabled={isSubmitting}
                className="ntf-file-input"
              />
            </div>
            {formData.attachments.length > 0 && (
              <ul className="ntf-file-list">
                {formData.attachments.map((file, index) => (
                  <li key={index}>
                    {file.name} 
                    <button type="button" onClick={() => removeAttachment(index)} disabled={isSubmitting}>&times;</button>
                  </li>
                ))}
              </ul>
            )}
          </div>

          {errors.submit && <div className="ntf-error-text ntf-submit-error">{errors.submit}</div>}

          <div className="ntf-form-actions">
            <button type="button" onClick={onClose} className="ntf-btn-cancel" disabled={isSubmitting}>
              Cancel
            </button>
            <button type="submit" className="ntf-btn-submit" disabled={isSubmitting}>
              {isSubmitting ? 'Submitting...' : 'Submit'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default NewTicketForm;
