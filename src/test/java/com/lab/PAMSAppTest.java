package com.lab;

import com.lab.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PAMSAppTest {
    private static final Clock FIXED_CLOCK = Clock.fixed(Instant.parse("2026-04-01T12:00:00Z"), ZoneOffset.UTC);

    @TempDir
    Path tempDir;

    @Test
    void createsPatientsUsingAssignmentData() {
        Patient[] patients = PAMSApp.createPatients();

        assertEquals(5, patients.length);
        assertEquals("Dana", patients[0].getFirstName());
        assertEquals("Washington", patients[4].getLastName());
        assertEquals(LocalDate.of(1932, 5, 31), patients[4].getDateOfBirth());
    }

    @Test
    void sortsPatientsByDescendingAge() {
        List<Patient> patients = PAMSApp.getPatientsSortedByDescendingAge(FIXED_CLOCK);

        assertEquals(List.of("5", "1", "2", "4", "3"),
                patients.stream().map(Patient::getPatientId).toList());
        assertEquals(93, patients.getFirst().getAge(FIXED_CLOCK));
        assertEquals(24, patients.getLast().getAge(FIXED_CLOCK));
    }

    @Test
    void writesJsonFileIncludingAge() throws IOException {
        Path outputPath = tempDir.resolve("patients.json");

        PAMSApp.writePatientsJson(outputPath, FIXED_CLOCK);

        String json = Files.readString(outputPath);
        assertTrue(json.contains("\"patientId\": \"5\""));
        assertTrue(json.contains("\"age\": 93"));
        assertTrue(json.contains("\"email\": \"\""));
    }
}
