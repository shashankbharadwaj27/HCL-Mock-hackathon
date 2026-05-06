import api from "./axiosInstance";

export const getContacts = async (search = "") => {
  const res = await api.get(`/contacts?search=${search}`);
  return res.data;
};

export const getContactById = async (id) => {
  const res = await api.get(`/contacts/${id}`);
  return res.data;
};

export const createContact = async (data) => {
  const res = await api.post("/contacts", data);
  return res.data;
};

export const updateContact = async (id, data) => {
  const res = await api.put(`/contacts/${id}`, data);
  return res.data;
};

export const deleteContact = async (id) => {
  const res = await api.delete(`/contacts/${id}`);
  return res.data;
};