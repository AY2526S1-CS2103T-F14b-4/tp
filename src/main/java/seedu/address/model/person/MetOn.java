package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents the date the user met a contact for the first time.
 */
public class MetOn {

    public final LocalDateTime localDateTime;

    public MetOn(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    /**
     * Returns a string representation of the time.
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy (HH:mm)", Locale.ENGLISH);
        return localDateTime.format(formatter);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof MetOn o)) {
            return false;
        }
        return localDateTime.equals(o.localDateTime);
    }

    @Override
    public int hashCode() {
        return localDateTime.hashCode();
    }
}
