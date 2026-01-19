import React, { useState, useEffect, useRef } from 'react';
import Avatar from './Avatar';

const ContactModal = ({ show, onClose, onSave, initialData }) => {
    const [formData, setFormData] = useState({ name: '', phone: '', email: '', address: '' });
    const [file, setFile] = useState(null);
    const [previewUrl, setPreviewUrl] = useState(null);

    const fileInputRef = useRef(null);

    useEffect(() => {
        if (show) {
            if (initialData) {
                // edit mode: reload contact data and fill the form
                setFormData({
                    name: initialData.name || '',
                    phone: initialData.phone || '',
                    email: initialData.email || '',
                    address: initialData.address || ''
                });
                setPreviewUrl(null);
            } else {
                // add mode: empty form
                setFormData({ name: '', phone: '', email: '', address: '' });
                setPreviewUrl(null);
            }
            setFile(null);
        }
    }, [show, initialData]);

    if (!show) return null;

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const triggerFileSelect = () => {
        fileInputRef.current.click();
    };

    const handleFileChange = (e) => {
        const selectedFile = e.target.files[0];
        if (selectedFile) {
            setFile(selectedFile);
            setPreviewUrl(URL.createObjectURL(selectedFile));
        }
    };

    const handleSubmit = () => {
        if (!formData.name.trim()) {
            alert("Name is required!");
            return;
        }
        onSave(formData, file);
    };

    return (
        <div className="modal show d-block" style={{ backgroundColor: 'rgba(0,0,0,0.7)', backdropFilter: 'blur(5px)' }}>
            <div className="modal-dialog modal-dialog-centered">
                <div className="modal-content custom-modal-content p-4">

                    <div className="modal-header justify-content-center border-0 mb-2">
                        <h4 className="fw-bold mb-0">{initialData ? 'Edit contact' : 'Add contact'}</h4>
                    </div>

                    <div className="modal-body text-center">
                        {/* image upload */}
                        <div className="d-flex flex-column align-items-center mb-4">
                            <Avatar
                                size={100}
                                editable={true}
                                imagePath={initialData?.imagePath} // old img
                                previewUrl={previewUrl}            // new img
                                onFileSelect={triggerFileSelect}
                            />
                            <button className="btn btn-text-custom mt-2" onClick={triggerFileSelect}>
                                {initialData || previewUrl ? 'Change picture' : 'Add picture'}
                            </button>
                            <input type="file" ref={fileInputRef} hidden accept="image/*" onChange={handleFileChange} />
                        </div>

                        {/* input fields */}
                        <div className="text-start">
                            <label className="dark-label">Name</label>
                            <input
                                type="text" className="dark-input" name="name"
                                value={formData.name} onChange={handleInputChange} placeholder="Gipsz Jakab"
                            />

                            <label className="dark-label">Phone number</label>
                            <input
                                type="text" className="dark-input" name="phone"
                                value={formData.phone} onChange={handleInputChange} placeholder="+36 01 234 5678"
                            />

                            <label className="dark-label">Email address</label>
                            <input
                                type="email" className="dark-input" name="email"
                                value={formData.email} onChange={handleInputChange} placeholder="gipsz.jakab@mail.com"
                            />

                            <label className="dark-label">Address</label>
                            <input
                                type="text" className="dark-input" name="address"
                                value={formData.address} onChange={handleInputChange} placeholder="Budapest"
                            />
                        </div>
                    </div>

                    <div className="modal-footer justify-content-between">
                        <button className="btn btn-text-custom text-white" onClick={onClose}>Cancel</button>
                        <button className="btn btn-primary-custom" onClick={handleSubmit}>Done</button>
                    </div>

                </div>
            </div>
        </div>
    );
};

export default ContactModal;