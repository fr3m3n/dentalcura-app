package clinic.entities;



public record Patient(Long id, String name, String surname, String address, int niNumber, String registrationDate) {

    @Override
    public String toString() {
        return "Patient: " + name + " " + surname +
                ", ni_number: " + niNumber;
    }
}
