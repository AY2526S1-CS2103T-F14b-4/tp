package seedu.address.ui;

import javafx.beans.property.SimpleStringProperty;

/**
 * Represents a single country entry in the HelpWindow table.
 * Each entry has:
 * - The country name
 * - The country code
 */
public class CountryEntry {
    private final SimpleStringProperty name;
    private final SimpleStringProperty code;

    /**
     * Constructs a CountryEntry.
     *
     * @param name The name of the country.
     * @param code The unique telephone code of the country.
     */
    public CountryEntry(String name, String code) {
        this.name = new SimpleStringProperty(name);
        this.code = new SimpleStringProperty(code);
    }

    /**
     * Returns the name of this country.
     *
     * @return country name as a String
     */
    public String getName() {
        return name.get();
    }

    /**
     * Returns the country code of this country entry.
     *
     * @return format and examples as a String
     */
    public String getCode() {
        return code.get();
    }
}
