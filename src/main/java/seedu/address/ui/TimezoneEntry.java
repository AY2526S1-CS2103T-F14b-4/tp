package seedu.address.ui;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Represents a time zone entry for display in a UI table.
 * <p>
 * Each entry consists of a {@code region} (typically the city or region name)
 * and its corresponding UTC {@code offset} in the format "UTC±HH:MM".
 * </p>
 */
public class TimezoneEntry {
    private final String region;
    private final String offset;

    public TimezoneEntry(String region, String offset) {
        this.region = region;
        this.offset = offset;
    }

    public String getRegion() {
        return region;
    }

    public String getOffset() {
        return offset;
    }

}
