public class Client {
    private String givenName;
    private String surname;
    private String email;
    private int numberOfGuests;

    public Client(String givenName, String surname, String email, int numberOfGuests) {
        if (givenName == null || givenName.isBlank()) throw new IllegalArgumentException("givenName required");
        if (surname == null || surname.isBlank()) throw new IllegalArgumentException("givenName required");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("givenName required");
        if (numberOfGuests <= 0) throw new IllegalArgumentException("numberOfGuests must be > 0");

        this.givenName = givenName.trim();
        this.surname = surname.trim();
        this.email = email.trim().toLowerCase(); // normalize email
        this.numberOfGuests = numberOfGuests;
    }

    public String getFullName() { return givenName + " " + surname; }
    public String getGivenName() { return givenName; }
    public String getSurname() { return surname; }
    public String getEmail() { return email; }
    public int getNumberOfGuests() { return numberOfGuests; }
}
