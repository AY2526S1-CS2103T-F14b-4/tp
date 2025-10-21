package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.ZoneOffset;

/**
 * Represents a GMT offset for a person.
 * Guarantees: valid format +HH:MM or -HH:MM, immutable.
 */
public class Offset implements Comparable<Offset> {

    public static final String MESSAGE_CONSTRAINTS =
            "GMT offset must be in the format +HH:MM or -HH:MM, where HH is 00-14 and MM is 00-59.";
    public static final String VALIDATION_REGEX = "^[+-](?:0\\d|1[0-4]):[0-5]\\d$";

    public String value;
    private final int totalMinutes; // offset in minutes


    /**
     * Constructs an {@code Offset} from a string.
     *
     * @param input GMT offset in the format +HH:MM or -HH:MM
     */
    public Offset(String input) {
        checkArgument(isValidOffset(input), MESSAGE_CONSTRAINTS);
        this.value = input;

        String[] parts = input.substring(1).split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);

        this.totalMinutes = (input.charAt(0) == '+' ? 1 : -1) * (hours * 60 + minutes);
    }

    public static boolean isValidOffset(String test) {
        return test != null && test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns the total offset in minutes.
     */
    public int getTotalMinutes() {
        return totalMinutes;
    }

    /**
     * Returns a string representation in +HH:MM or -HH:MM format.
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Compares two Offsets by their total minutes.
     */
    @Override
    public int compareTo(Offset other) {
        return Integer.compare(this.totalMinutes, other.totalMinutes);
    }

    /**
     * Converts to a {@link ZoneOffset}.
     */
    public ZoneOffset toZoneOffset() {
        return ZoneOffset.of(value);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Offset o)) {
            return false;
        }
        return value.equals(o.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
