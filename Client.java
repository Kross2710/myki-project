public class Client {
    private String givenName;
    private String surname;
    private String email;
    private int numberOfGuests;

    public Client(String givenName, String surname, String email, int numberOfGuests) {
        this.givenName = givenName;
        this.surname = surname;
        this.email = email;
        this.numberOfGuests = numberOfGuests;
    }

    public String getFullName() {
        return givenName + " " + surname;
    }

    public String getEmail() {
        return email;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
