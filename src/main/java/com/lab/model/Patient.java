package com.lab.model;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class Patient {
    private final String patientId;
    private final String firstName;
    private final String lastName;
    private final String contactPhoneNumber;
    private final String email;
    private final String mailingAddress;
    private final LocalDate dateOfBirth;

    public Patient(
            String patientId,
            String firstName,
            String lastName,
            String contactPhoneNumber,
            String email,
            String mailingAddress,
            LocalDate dateOfBirth
    ) {
        this.patientId = requireText(patientId, "Patient ID is required.");
        this.firstName = requireText(firstName, "First name is required.");
        this.lastName = requireText(lastName, "Last name is required.");
        this.contactPhoneNumber = normalizeOptionalText(contactPhoneNumber);
        this.email = normalizeOptionalText(email);
        this.mailingAddress = normalizeOptionalText(mailingAddress);
        this.dateOfBirth = Objects.requireNonNull(dateOfBirth, "Date of birth is required.");
    }

    public String getPatientId() {
        return patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public int getAge() {
        return getAge(Clock.systemDefaultZone());
    }

    public int getAge(Clock clock) {
        return Period.between(dateOfBirth, LocalDate.now(clock)).getYears();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientId='" + patientId + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", contactPhoneNumber='" + contactPhoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", mailingAddress='" + mailingAddress + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    private static String normalizeOptionalText(String value) {
        return value == null ? "" : value.trim();
    }
}
