package lv.bootcamp.shelter.task23;

import lv.bootcamp.shelter.model.Animal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tasks 2 & 3: Parameterized tests and exception tests
 *
 * Practice:
 * - @ParameterizedTest with @CsvSource
 * - @ValueSource and @NullAndEmptySource
 * - assertThrows with message checks
 * - AssertJ assertThatThrownBy
 *
 * Instructions:
 * Write tests for AnimalValidator. Each describes one test to write.
 */
@DisplayName("AnimalValidator")
class AnimalValidatorTest {

    private AnimalValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AnimalValidator();
    }

    // ==================== Task 2: Parameterized tests ====================

    @Nested
    @DisplayName("validateName")
    class ValidateName {

        @ParameterizedTest
        @ValueSource(strings = {"Buddy", "Luna", "Mr. Whiskers", "X"})
        @DisplayName("accepts valid names")
        void shouldAcceptValidNames(String name) {
            assertDoesNotThrow(() -> validator.validateName(name));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("rejects blank or null names")
        void shouldRejectBlankNames(String name) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validateName(name));
            assertThat(ex).hasMessageContaining("must not be blank");
        }

        @Test
        @DisplayName("rejects name longer than 100 characters")
        void shouldRejectOverlyLongName() {
            String testString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validateName(testString));
            assertThat(ex).hasMessageContaining("100 characters");
        }
    }

    @Nested
    @DisplayName("validateAge")
    class ValidateAge {

        @ParameterizedTest
        @CsvSource({
                "0",
                "1",
                "10",
                "50"
        })
        @DisplayName("accepts valid ages")
        void shouldAcceptValidAges(int age) {
            assertDoesNotThrow(() -> validator.validateAge(age));
        }

        @ParameterizedTest
        @CsvSource({
                "-1, must not be negative",
                "-100, must not be negative",
                "51, seems unrealistic",
                "999, seems unrealistic"
        })
        @DisplayName("rejects invalid ages with correct message")
        void shouldRejectInvalidAges(int age, String expectedMessagePart) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validateAge(age));
            assertThat(ex).hasMessageContaining(expectedMessagePart);
        }
    }

    // ==================== Task 3: Exception tests ====================

    @Nested
    @DisplayName("validate (full animal)")
    class ValidateFullAnimal {

        @Test
        @DisplayName("throws NullPointerException for null animal")
        void shouldThrowForNullAnimal() {
            NullPointerException ex = assertThrows(NullPointerException.class, () -> validator.validate(null));
            assertThat(ex).hasMessageContaining("must not be null");

        }

        @Test
        @DisplayName("throws for animal with blank name")
        void shouldThrowForBlankName() {
            Animal animal = new Animal("", "Dog", 12, false, LocalDate.now());
            assertThrows(IllegalArgumentException.class, () -> validator.validate(animal));
        }

        @Test
        @DisplayName("throws for animal with blank species")
        void shouldThrowForBlankSpecies() {
            Animal animal = new Animal("Bob", "", 12, false, LocalDate.now());
            assertThrows(IllegalArgumentException.class, () -> validator.validate(animal));
        }

        @Test
        @DisplayName("throws for animal with negative age")
        void shouldThrowForNegativeAge() {
            Animal animal = new Animal("Bob", "Dog", -12, false, LocalDate.now());
            assertThrows(IllegalArgumentException.class, () -> validator.validate(animal));
        }

        @Test
        @DisplayName("does not throw for fully valid animal")
        void shouldPassForValidAnimal() {
            Animal animal = new Animal("Buddy", "Dog", 3, true, LocalDate.now());
            assertDoesNotThrow(() -> validator.validate(animal));
        }
    }
}
