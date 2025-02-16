import java.util.*;
import java.util.stream.Collectors;

public class ContactService {
    private Map<String, Contact> contacts;  
    private TreeMap<String, Contact> sortedByFirstName;  
    private TreeMap<String, Contact> sortedByLastName;  

    public ContactService() {
        contacts = new LinkedHashMap<>();
        sortedByFirstName = new TreeMap<>(Comparator.comparing(String::toLowerCase));  
        sortedByLastName = new TreeMap<>(Comparator.comparing(String::toLowerCase));  
    }

    // Add a contact with validation
    public void addContact(Contact contact) {
        if (contact == null || contact.getContactId() == null || contact.getContactId().isEmpty()) {
            throw new IllegalArgumentException("Contact or Contact ID cannot be null or empty.");
        }
        if (contacts.containsKey(contact.getContactId())) {
            throw new IllegalArgumentException("Contact ID already exists.");
        }
        validateContact(contact);
        contacts.put(contact.getContactId(), contact);
        sortedByFirstName.put(contact.getFirstName().toLowerCase() + contact.getContactId(), contact);
        sortedByLastName.put(contact.getLastName().toLowerCase() + contact.getContactId(), contact);
    }

    // Delete a contact
    public void deleteContact(String contactId) {
        if (!contacts.containsKey(contactId)) {
            throw new IllegalArgumentException("Contact ID not found.");
        }
        Contact removed = contacts.remove(contactId);
        sortedByFirstName.remove(removed.getFirstName().toLowerCase() + contactId);
        sortedByLastName.remove(removed.getLastName().toLowerCase() + contactId);
    }

    // Update an existing contact
    public void updateContact(String contactId, String firstName, String lastName, String phone, String address) {
        Contact contact = contacts.get(contactId);
        if (contact == null) {
            throw new IllegalArgumentException("Contact ID not found.");
        }
        validateInput("First Name", firstName);
        validateInput("Last Name", lastName);
        validatePhone(phone);
        validateInput("Address", address);

        // Remove old entries before updating
        sortedByFirstName.remove(contact.getFirstName().toLowerCase() + contactId);
        sortedByLastName.remove(contact.getLastName().toLowerCase() + contactId);

        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setPhone(phone);
        contact.setAddress(address);

        // Add updated entries
        sortedByFirstName.put(contact.getFirstName().toLowerCase() + contactId, contact);
        sortedByLastName.put(contact.getLastName().toLowerCase() + contactId, contact);
    }

    // Get all contacts
    public Map<String, Contact> getContacts() {
        return new LinkedHashMap<>(contacts);
    }

    // Optimized search by name using TreeMap for efficiency
    public List<Contact> searchContacts(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Search name cannot be null or empty.");
        }
        String lowerName = name.toLowerCase();
        return contacts.values().stream()
                .filter(contact -> contact.getFirstName().equalsIgnoreCase(lowerName) ||
                                   contact.getLastName().equalsIgnoreCase(lowerName))
                .collect(Collectors.toList());
    }

    // Efficiently sort contacts by first name using TreeMap
    public List<Contact> sortContactsByFirstName() {
        return new ArrayList<>(sortedByFirstName.values());
    }

    // Efficiently sort contacts by last name using TreeMap
    public List<Contact> sortContactsByLastName() {
        return new ArrayList<>(sortedByLastName.values());
    }

    // Validate contact details
    private void validateContact(Contact contact) {
        validateInput("First Name", contact.getFirstName());
        validateInput("Last Name", contact.getLastName());
        validatePhone(contact.getPhone());
        validateInput("Address", contact.getAddress());
    }

    // Validate input fields
    private void validateInput(String fieldName, String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty.");
        }
    }

    // Validate phone number format
    private void validatePhone(String phone) {
        if (phone == null || !phone.matches("\\d{10}")) {
            throw new IllegalArgumentException("Phone number must be exactly 10 digits.");
        }
    }
}
