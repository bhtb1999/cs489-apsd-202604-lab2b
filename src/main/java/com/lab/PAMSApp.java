package com.lab;

import com.lab.model.Patient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PAMSApp {
    public static void main(String[] args) throws IOException {
        Path outputPath = Path.of("patients.json");
        writePatientsJson(outputPath, Clock.systemDefaultZone());
        System.out.println("Wrote patient data to " + outputPath.toAbsolutePath());
    }

    static Patient[] createPatients() {
        return new Patient[]{
                new Patient("1", "Dana", "Agar", "(641) 123-0009", "dagar@m.as", "1 N Street", LocalDate.of(1984, 12, 19)),
                new Patient("2", "Ana", "Smith", "", "amsith@te.edu", "", LocalDate.of(1984, 12, 5)),
                new Patient("3", "Marcus", "Garvey", "(123) 292-0018", "", "4 East Ave", LocalDate.of(2001, 9, 18)),
                new Patient("4", "Jeff", "Goldbloom", "(999) 165-1192", "jgold@es.co.za", "", LocalDate.of(1995, 2, 28)),
                new Patient("5", "Mary", "Washington", "", "", "30 W Burlington", LocalDate.of(1932, 5, 31))
        };
    }

    static List<Patient> getPatientsSortedByDescendingAge(Clock clock) {
        return Arrays.stream(createPatients())
                .sorted(Comparator.comparingInt((Patient patient) -> patient.getAge(clock)).reversed())
                .toList();
    }

    static void writePatientsJson(Path outputPath, Clock clock) throws IOException {
        Files.writeString(outputPath, toJson(getPatientsSortedByDescendingAge(clock), clock), StandardCharsets.UTF_8);
    }

    static String toJson(List<Patient> patients, Clock clock) {
        StringBuilder json = new StringBuilder();
        json.append("[\n");

        for (int index = 0; index < patients.size(); index++) {
            Patient patient = patients.get(index);
            json.append("  {\n");
            json.append("    \"patientId\": \"").append(escapeJson(patient.getPatientId())).append("\",\n");
            json.append("    \"firstName\": \"").append(escapeJson(patient.getFirstName())).append("\",\n");
            json.append("    \"lastName\": \"").append(escapeJson(patient.getLastName())).append("\",\n");
            json.append("    \"contactPhoneNumber\": \"").append(escapeJson(patient.getContactPhoneNumber())).append("\",\n");
            json.append("    \"email\": \"").append(escapeJson(patient.getEmail())).append("\",\n");
            json.append("    \"mailingAddress\": \"").append(escapeJson(patient.getMailingAddress())).append("\",\n");
            json.append("    \"dateOfBirth\": \"").append(patient.getDateOfBirth()).append("\",\n");
            json.append("    \"age\": ").append(patient.getAge(clock)).append('\n');
            json.append("  }");
            if (index < patients.size() - 1) {
                json.append(',');
            }
            json.append('\n');
        }

        json.append("]\n");
        return json.toString();
    }

    private static String escapeJson(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}
