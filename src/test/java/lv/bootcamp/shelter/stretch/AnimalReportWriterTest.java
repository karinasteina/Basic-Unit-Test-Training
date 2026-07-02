package lv.bootcamp.shelter.stretch;

import lv.bootcamp.shelter.model.Animal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Stretch goal: Testing file output
 *
 * Practice:
 * - Writing to temp files and reading them back
 * - String content assertions
 * - Cleanup with Files.deleteIfExists
 *
 * Instructions:
 * These tests verify that AnimalReportWriter produces correct output.
 * This task is optional — attempt it after completing tasks 1–6.
 */
@DisplayName("AnimalReportWriter (stretch)")
class AnimalReportWriterTest {

    private final AnimalReportWriter writer = new AnimalReportWriter();
    Animal buddy = new Animal("Buddy", "Dog", 3, true, LocalDate.of(2026, 1, 15));
    Animal luna = new Animal("Luna", "Cat", 2, true, LocalDate.of(2026, 1, 10));
    Animal max = new Animal("Max", "Dog", 5, false, LocalDate.of(2026, 1, 20));


    @Test
    @DisplayName("writes report file that contains total count")
    void shouldWriteTotalCount() throws IOException {
        List<Animal> animals = new ArrayList<>(List.of(buddy, luna, max));

        Path output = Files.createTempFile("report-test", ".txt");

        writer.writeReport(animals, output);

        String content = Files.readString(output, StandardCharsets.UTF_8);

        assertTrue(content.contains("Total animals: 3"));

        Files.deleteIfExists(output);
    }

    @Test
    @DisplayName("writes per-species breakdown in alphabetical order")
    void shouldWriteSpeciesBreakdown() throws IOException {
        List<Animal> animals = new ArrayList<>(List.of(luna, max));

        Path output = Files.createTempFile("report-test", ".txt");
        writer.writeReport(animals, output);

        String content = Files.readString(output, StandardCharsets.UTF_8);

        String[] lines = content.split("\n");

        int catLineIndex = 0;
        int dogLineIndex = 0;
        
        for (int i = 0; i < lines.length; i++) {
            if(lines[i].startsWith("Cat:")){
                catLineIndex = i;
            }
            
            if(lines[i].startsWith("Dog:")){
                dogLineIndex = i;
            }
        }
        
        assertTrue(catLineIndex < dogLineIndex);

        assertTrue(content.contains("Vaccinated: 1"));

        Files.deleteIfExists(output);
    }

    @Test
    @DisplayName("writes oldest animal per species")
    void shouldWriteOldestPerSpecies() throws IOException {

        List<Animal> animals = new ArrayList<>(List.of(buddy, luna, max));

        Path output = Files.createTempFile("report-test", ".txt");
        writer.writeReport(animals, output);

        String content = Files.readString(output, StandardCharsets.UTF_8);
        assertTrue(content.contains("Dog: Max (age 5)"));
    }
}
