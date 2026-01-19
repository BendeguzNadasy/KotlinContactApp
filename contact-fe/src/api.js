import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

const api = axios.create({
    baseURL: API_BASE_URL,
});

export const getContacts = async () => {
    const response = await api.get('/api/contacts');
    return response.data;
};

export const createContact = async (contactData, imageFile) => {
    const formData = new FormData();
    formData.append('name', contactData.name);
    formData.append('email', contactData.email);
    formData.append('phone', contactData.phone);
    formData.append('address', contactData.address);

    if (imageFile) {
        formData.append('image', imageFile);
    }

    const response = await api.post('/api/contacts', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
    });
    return response.data;
};

export const updateContact = async (id, contactData, imageFile) => {
    const formData = new FormData();
    formData.append('name', contactData.name);
    formData.append('email', contactData.email);
    formData.append('phone', contactData.phone);
    formData.append('address', contactData.address);

    if (imageFile) {
        formData.append('image', imageFile);
    }

    const response = await api.put(`/api/contacts/${id}`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
    });
    return response.data;
};

export const deleteContact = async (id) => {
    await api.delete(`/api/contacts/${id}`);
};

export const getImageUrl = (filename) => {
    if (!filename) return null;
    return `${API_BASE_URL}/api/contacts/images/${filename}`;
};