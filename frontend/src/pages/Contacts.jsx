import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import useAuth from "../hooks/useAuth";

import ContactCard from "../components/ContactCard";

import {
  getContacts,
  deleteContact,
} from "../api/contactApi";

const Contacts = () => {
  const navigate = useNavigate();

  const { user, logout } = useAuth();

  const [contacts, setContacts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState("");

  const fetchContacts = async () => {
    try {
      setLoading(true);

      const data = await getContacts(search);

      setContacts(data.contacts || []);
    } catch (err) {
      console.log(err);
      alert("Failed to fetch contacts");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchContacts();
  }, [search]);

  const handleDelete = async (id) => {
    const confirmDelete = window.confirm(
      "Delete this contact?"
    );

    if (!confirmDelete) return;

    try {
      await deleteContact(id);

      fetchContacts();
    } catch (err) {
      alert("Delete failed");
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      {/* Header */}

      <div className="bg-white shadow px-8 py-4 flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold">
            Contacts Dashboard
          </h1>

          <p className="text-gray-500">
            Welcome, {user?.email}
          </p>
        </div>

        <button
          onClick={logout}
          className="bg-red-500 hover:bg-red-600 text-white px-5 py-2 rounded-lg"
        >
          Logout
        </button>
      </div>

      {/* Search + Add */}

      <div className="p-8 flex flex-col md:flex-row gap-4 justify-between">
        <input
          type="text"
          placeholder="Search contacts..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="bg-white px-4 py-3 rounded-xl shadow outline-none w-full md:w-96"
        />

        <button
          onClick={() => navigate("/contacts/new")}
          className="bg-blue-600 hover:bg-blue-700 text-white px-5 py-3 rounded-xl"
        >
          Add Contact
        </button>
      </div>

      {/* Content */}

      <div className="px-8 pb-8">
        {loading ? (
          <div className="text-center text-xl">
            Loading contacts...
          </div>
        ) : contacts.length === 0 ? (
          <div className="bg-white rounded-xl shadow p-10 text-center">
            <h2 className="text-2xl font-bold mb-3">
              No Contacts Found
            </h2>

            <p className="text-gray-500 mb-5">
              Add your first contact to get started.
            </p>

            <button
              onClick={() => navigate("/contacts/new")}
              className="bg-blue-600 hover:bg-blue-700 text-white px-5 py-3 rounded-lg"
            >
              Add Contact
            </button>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {contacts.map((contact) => (
              <ContactCard
                key={contact.id}
                contact={contact}
                onDelete={handleDelete}
                onClick={() =>
                  navigate(`/contacts/${contact.id}`)
                }
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default Contacts;