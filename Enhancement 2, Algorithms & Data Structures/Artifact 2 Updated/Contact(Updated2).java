public class Contact {
    private final String contactId; // Made immutable
    private String firstName;
    private String lastName;
    private String phone;
    private String address;

    public Contact(String contactId, String firstName, String lastName, String phone, String address) {
        validateInput("Contact ID", contactId, 10);
        validateInput("First Name", firstName, 20);
        validateInput("Last Name", lastName, 20);
        validatePhone(phone);
        validateInput("Address", address, 50);

        this.contactId = contactId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
    }

    public String getContactId() {
        return contactId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        validateInput("First Name", firstName, 20);
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        validateInput("Last Name", lastName, 20);
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        validatePhone(phone);
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        validateInput("Address", address, 50);
        this.address = address;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Contact{")
                .append("contactId='").append(contactId).append('\'')
                .append(", firstName='").append(firstName).append('\'')
                .append(", lastName='").append(lastName).append('\'')
                .append(", phone='").append(phone).append('\'')
                .append(", address='").append(address).append('\'')
                .append('}')
                .toString();
    }

    private void validateInput(String fieldName, String value, int maxLength) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty.");
        }
        if (value.length() > maxLength) {
            throw new IllegalArgumentException(fieldName + " cannot exceed " + maxLength + " characters.");
        }
    }

    private void validatePhone(String phone) {
        if (phone == null || !phone.matches("\\d{10}")) {
            throw new IllegalArgumentException("Phone number must be exactly 10 digits.");
        }
    }
}
