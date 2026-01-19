import React from 'react';
import Avatar from './Avatar';

const ContactList = ({ contacts, onEdit, onDelete }) => {
    if (!contacts || contacts.length === 0) {
        return <div className="text-center mt-5 text-secondary">No contacts found.</div>;
    }

    return (
        <div className="container mt-4">
            {contacts.map((contact) => (
                <div key={contact.id} className="contact-row d-flex align-items-center justify-content-between">

                    {/* Bal oldal: Avatar + Adatok */}
                    <div className="d-flex align-items-center" onClick={() => onEdit(contact)}>
                        <div className="me-3">
                            <Avatar size={45} imagePath={contact.imagePath} />
                        </div>
                        <div>
                            <h6 className="mb-0 text-white">{contact.name}</h6>
                            {contact.phone && <small style={{color: '#8e8e93'}}>{contact.phone}</small>}
                        </div>
                    </div>

                    {/* Jobb oldal: Akciók (A képen ... menü van, itt egyszerűsítve ikonok) */}
                    <div className="d-flex gap-3">
                        {/* Edit gomb */}
                        <button className="action-menu-btn" onClick={(e) => { e.stopPropagation(); onEdit(contact); }}>
                            <i className="bi bi-pencil"></i>
                        </button>
                        {/* Delete gomb (Remove flow) */}
                        <button className="action-menu-btn text-danger" onClick={(e) => { e.stopPropagation(); onDelete(contact.id); }}>
                            <i className="bi bi-trash"></i>
                        </button>
                    </div>

                </div>
            ))}
        </div>
    );
};

export default ContactList;