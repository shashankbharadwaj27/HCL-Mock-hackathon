const ContactCard = ({ contact, onDelete, onClick }) => {
  const primaryPhone =
    contact.phones?.find((p) => p.isPrimary)?.number ||
    contact.phones?.[0]?.number;

  return (
    <div className="bg-white rounded-xl shadow-md p-5 flex flex-col gap-3 hover:shadow-lg transition-all">
      <div onClick={onClick} className="cursor-pointer">
        <h2 className="text-xl font-bold">
          {contact.firstName} {contact.lastName}
        </h2>

        <p className="text-gray-600">{contact.email}</p>

        <p className="text-gray-800 font-medium">
          {primaryPhone || "No phone"}
        </p>
      </div>

      <button
        onClick={() => onDelete(contact.id)}
        className="bg-red-500 hover:bg-red-600 text-white py-2 rounded-lg"
      >
        Delete
      </button>
    </div>
  );
};

export default ContactCard;