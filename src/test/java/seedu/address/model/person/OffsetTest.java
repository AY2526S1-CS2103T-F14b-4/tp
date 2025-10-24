package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class OffsetTest {

    @Test
    public void isValidOffset_null_false() {
        assertFalse(Offset.isValidOffset(null));
    }

    @Test
    public void isValidOffset_invalidFormats_false() {
        // Missing sign
        assertFalse(Offset.isValidOffset("08:00"));
        // Wrong separators/length
        assertFalse(Offset.isValidOffset("+8:00"));
        assertFalse(Offset.isValidOffset("+0800"));
        assertFalse(Offset.isValidOffset("+08-00"));
        // Out of range hours
        assertFalse(Offset.isValidOffset("+15:00"));
        assertFalse(Offset.isValidOffset("-99:00"));
        // Out of range minutes
        assertFalse(Offset.isValidOffset("+08:60"));
        assertFalse(Offset.isValidOffset("-02:99"));
        // Garbage
        assertFalse(Offset.isValidOffset("abc"));
        assertFalse(Offset.isValidOffset(""));
        assertFalse(Offset.isValidOffset("   "));
    }

    @Test
    public void isValidOffset_validFormats_true() {
        assertTrue(Offset.isValidOffset("+00:00"));
        assertTrue(Offset.isValidOffset("-00:00"));
        assertTrue(Offset.isValidOffset("+01:30"));
        assertTrue(Offset.isValidOffset("-05:45"));
        assertTrue(Offset.isValidOffset("+14:00")); // upper hour bound allowed by regex
        assertTrue(Offset.isValidOffset("-13:59"));
    }

    @Test
    public void constructor_invalid_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Offset("08:00"));    // no sign
        assertThrows(IllegalArgumentException.class, () -> new Offset("+15:00"));   // hour > 14
        assertThrows(IllegalArgumentException.class, () -> new Offset("+01:60"));   // minutes >= 60
        assertThrows(IllegalArgumentException.class, () -> new Offset("abc"));      // junk
    }

    @Test
    public void getTotalMinutes_signAndMath() {
        assertEquals(0, new Offset("+00:00").getTotalMinutes());
        assertEquals(0, new Offset("-00:00").getTotalMinutes());     // negative zero should still be 0
        assertEquals(90, new Offset("+01:30").getTotalMinutes());
        assertEquals(-345, new Offset("-05:45").getTotalMinutes());
        assertEquals(14 * 60, new Offset("+14:00").getTotalMinutes());
        assertEquals(-(13 * 60 + 59), new Offset("-13:59").getTotalMinutes());
    }

    @Test
    public void toString_roundTrip() {
        String s = "+09:30";
        assertEquals(s, new Offset(s).toString());
    }

    @Test
    public void compareTo_ordersByMinutesAscending() {
        Offset a = new Offset("-05:00"); // -300
        Offset b = new Offset("-00:30"); // -30
        Offset c = new Offset("+00:00"); // 0
        Offset d = new Offset("+01:15"); // 75

        List<Offset> list = Arrays.asList(d, b, a, c);
        list.sort(Offset::compareTo);

        assertEquals(Arrays.asList(a, b, c, d), list);
    }

    @Test
    public void compareTo_equalOffsets_zero() {
        Offset x = new Offset("+08:00");
        Offset y = new Offset("+08:00");
        assertEquals(0, x.compareTo(y));
    }

    @Test
    public void toZoneOffset_matchesValue() {
        Offset off = new Offset("+07:45");
        ZoneOffset zo = off.toZoneOffset();
        assertEquals(ZoneOffset.of("+07:45"), zo);
        assertEquals("+07:45", zo.toString());
    }

    @Test
    public void equals_sameValue_true() {
        assertEquals(new Offset("+02:00"), new Offset("+02:00"));
    }

    @Test
    public void equals_differentValue_false() {
        assertNotEquals(new Offset("+02:00"), new Offset("+02:30"));
        assertNotEquals(new Offset("+00:00"), new Offset("-00:00")); // different canonical strings, even if minutes==0
    }

    @Test
    public void hashCode_consistentWithEquals() {
        Offset x1 = new Offset("+03:15");
        Offset x2 = new Offset("+03:15");
        assertEquals(x1, x2);
        assertEquals(x1.hashCode(), x2.hashCode());
    }
}
