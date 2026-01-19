import { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';

import './App.css';
import { getContacts, createContact, updateContact, deleteContact, getImageUrl } from './api';
import ContactModal from './components/ContactModal';

import sunIcon from './assets/Light mode.png';
import backArrow from './assets/Back arrow.png';
import addIcon from './assets/Add.png';
import settingsIcon from './assets/Settings.png';
import muteIcon from './assets/Mute.png';
import callIcon from './assets/Call.png';
import moreIcon from './assets/More.png';

import editIcon from './assets/Settings.png';
import heartIcon from './assets/Favourite.png';
import trashIcon from './assets/Delete.png';

import myProfilePic from './assets/idle-profile.png'

const AvatarDisplay = ({ imagePath, name }) => {
    const url = getImageUrl(imagePath);
    return (
        <div className="avatar-container me-3">
            {url ? (
                <img src={url} alt={name} style={{width: '100%', height: '100%', objectFit: 'cover'}} />
            ) : (
                <img src="https://via.placeholder.com/50" alt="Placeholder" style={{opacity: 0.5}} />
            )}
        </div>
    );
};

function App() {
    const [contacts, setContacts] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [editingContact, setEditingContact] = useState(null);

    const [openMenuId, setOpenMenuId] = useState(null);

    useEffect(() => { loadData(); }, []);

    const loadData = async () => {
        try {
            const data = await getContacts();
            setContacts(data);
        } catch (err) { console.error(err); }
    };

    const toggleMenu = (e, id) => {
        e.stopPropagation();
        if (openMenuId === id) {
            setOpenMenuId(null);
        } else {
            setOpenMenuId(id);
        }
    };

    const handleMenuAction = (e, action, contact) => {
        e.stopPropagation();
        setOpenMenuId(null);

        if (action === 'edit') {
            openEditModal(contact);
        } else if (action === 'delete') {
            handleDelete(contact.id);
        } else if (action === 'favourite') {
            alert("Favourite funkció még nincs kész, de a gomb működik! :)");
        }
    };

    useEffect(() => {
        const closeMenu = () => setOpenMenuId(null);
        window.addEventListener('click', closeMenu);
        return () => window.removeEventListener('click', closeMenu);
    }, []);

    const openAddModal = () => { setEditingContact(null); setShowModal(true); };
    const openEditModal = (contact) => { setEditingContact(contact); setShowModal(true); };
    const handleClose = () => { setShowModal(false); setEditingContact(null); };

    const handleSave = async (formData, file) => {
        try {
            if (editingContact) await updateContact(editingContact.id, formData, file);
            else await createContact(formData, file);
            handleClose(); await loadData();
        } catch (e) { alert("Error saving contact!\n" + e); }
    };

    const handleDelete = async (id) => {
        if (window.confirm("Delete contact?")) {
            await deleteContact(id); loadData();
        }
    };

    return (
        <div className="app-layout">

            {/* left side */}
            <div className="d-flex justify-content-end">
                <div className="arrow-container">
                    <img src={backArrow} alt="Light Mode" className="custom-icon" />
                </div>
            </div>

            {/* main center side */}
            <div className="main-container">

                <div className="header-row">
                    <h1 className="page-title">Contacts</h1>

                    <div className="header-actions">

                        <button className="icon-btn">
                            <img src={settingsIcon} alt="Settings" className="custom-icon" />
                        </button>

                        <img src={myProfilePic} alt="Profile" className="profile-icon-small" />

                        <img src={addIcon} alt="Add" className="custom-icon" onClick={openAddModal}/>

                    </div>
                </div>

                {/* --- contact list part --- */}
                <div className="contact-list">
                    {contacts.map(contact => (
                        <div
                            key={contact.id}
                            className="contact-row d-flex align-items-center justify-content-between"
                            onClick={() => {
                                openEditModal(contact)
                            }}
                        >
                            {/* --- left side: contact details --- */}
                            <div className="d-flex align-items-center">
                                <AvatarDisplay imagePath={contact.imagePath} name={contact.name} />
                                <div>
                                    <h5 className="mb-0 text-white fw-bold" style={{fontSize: '1rem'}}>{contact.name}</h5>
                                    <small style={{color: '#8e8e93', fontSize: '0.85rem'}}>{contact.phone}</small>
                                </div>
                            </div>

                            {/* right side: action items on hover */}
                            <div className={`row-actions ${openMenuId === contact.id ? 'menu-open' : ''}`}>

                                <img
                                    src={muteIcon}
                                    alt="Mute"
                                    className="action-icon-img"
                                    onClick={(e) => e.stopPropagation()}
                                />

                                <img
                                    src={callIcon}
                                    alt="Call"
                                    className="action-icon-img"
                                    onClick={(e) => e.stopPropagation()}
                                />

                                <div className="more-menu-container">
                                    <img
                                        src={moreIcon}
                                        alt="More"
                                        className="action-icon-img"
                                        onClick={(e) => toggleMenu(e, contact.id)}
                                    />

                                    {/* --- dropdown list will trigger if the id is matching --- */}
                                    {openMenuId === contact.id && (
                                        <div className="dropdown-menu-custom">

                                            <div className="dropdown-item-custom" onClick={(e) => handleMenuAction(e, 'edit', contact)}>
                                                <img src={editIcon} alt="" className="dropdown-icon" />
                                                <span>Edit</span>
                                            </div>

                                            <div className="dropdown-item-custom" onClick={(e) => handleMenuAction(e, 'favourite', contact)}>
                                                <img src={heartIcon} alt="" className="dropdown-icon" />
                                                <span>Favourite</span>
                                            </div>

                                            <div className="dropdown-item-custom text-danger-custom" onClick={(e) => handleMenuAction(e, 'delete', contact)}>
                                                <img src={trashIcon} alt="" className="dropdown-icon" />
                                                <span>Remove</span>
                                            </div>

                                        </div>
                                    )}
                                </div>
                            </div>
                        </div>
                    ))}

                    {contacts.length === 0 && (
                        <div className="text-center mt-5 text-secondary">There are no contacts.</div>
                    )}
                </div>

            </div>

            <div className="d-flex justify-content-end">
                <div className="sun-icon-container">
                    <img src={sunIcon} alt="Light Mode" className="custom-icon" />
                </div>
            </div>

            {/* contact modal */}
            <ContactModal
                show={showModal}
                onClose={handleClose}
                onSave={handleSave}
                initialData={editingContact}
            />

        </div>
    );
}

export default App;