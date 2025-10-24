package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class MetOnTest {

    @Test
    public void toString_correctFormat() {
        // Arrange
        LocalDateTime dateTime = LocalDateTime.of(2005, 3, 24, 15, 0);
        MetOn metOn = new MetOn(dateTime);

        // Act
        String result = metOn.toString();

        // Assert
        assertEquals("24 March 2005 (15:00)", result);
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 10, 0);
        MetOn metOn = new MetOn(dateTime);
        assertEquals(metOn, metOn);
    }

    @Test
    public void equals_differentObjectSameDate_returnsTrue() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 10, 0);
        MetOn metOn1 = new MetOn(dateTime);
        MetOn metOn2 = new MetOn(LocalDateTime.of(2023, 1, 1, 10, 0));
        assertEquals(metOn1, metOn2);
    }

    @Test
    public void equals_differentDates_returnsFalse() {
        MetOn metOn1 = new MetOn(LocalDateTime.of(2023, 1, 1, 10, 0));
        MetOn metOn2 = new MetOn(LocalDateTime.of(2023, 1, 2, 10, 0));
        assertNotEquals(metOn1, metOn2);
    }

    @Test
    public void equals_null_returnsFalse() {
        MetOn metOn = new MetOn(LocalDateTime.of(2023, 1, 1, 10, 0));
        assertNotEquals(metOn, null);
    }

    @Test
    public void equals_differentType_returnsFalse() {
        MetOn metOn = new MetOn(LocalDateTime.of(2023, 1, 1, 10, 0));
        assertNotEquals(metOn, "not a MetOn object");
    }

    @Test
    public void hashCode_sameDate_returnsSameHashCode() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 10, 0);
        MetOn metOn1 = new MetOn(dateTime);
        MetOn metOn2 = new MetOn(LocalDateTime.of(2023, 1, 1, 10, 0));
        assertEquals(metOn1.hashCode(), metOn2.hashCode());
    }

    @Test
    public void hashCode_differentDates_returnsDifferentHashCode() {
        MetOn metOn1 = new MetOn(LocalDateTime.of(2023, 1, 1, 10, 0));
        MetOn metOn2 = new MetOn(LocalDateTime.of(2023, 1, 2, 10, 0));
        assertNotEquals(metOn1.hashCode(), metOn2.hashCode());
    }
}
