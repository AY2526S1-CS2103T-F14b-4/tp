package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Represents a Person's country in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Country {

    public static final String MESSAGE_CONSTRAINTS =
            "Country names are case sensitive and should match the list of countries available in the help window "
                    + "(type help to view).";

    /*
     * Allows alphabetic words separated by spaces.
     */
    public static final String VALIDATION_REGEX =
            "([\\p{L}][\\p{L} '\\-,.&()]*|\\p{L}+([\\p{L} '\\-,.&()]*[\\p{L}])?)?";

    /*
     * Construct set of valid country names for validation matching.
     */
    private static final Set<String> VALID_COUNTRY_NAMES = new HashSet<>();

    static {
        for (String countryCode : Locale.getISOCountries()) {
            Locale locale = new Locale("", countryCode);
            String countryName = locale.getDisplayCountry(Locale.ENGLISH);
            VALID_COUNTRY_NAMES.add(countryName);
        }
    }

    public final String value;

    /**
     * Constructs a {@code Country}.
     *
     * @param country A valid country.
     */
    public Country(String country) {
        checkArgument(isValidCountry(country), MESSAGE_CONSTRAINTS);
        value = country;
    }

    /**
     * Returns true if a given string is a valid country name.
     */
    public static boolean isValidCountry(String test) {
        boolean inCountryList = test.isEmpty() || VALID_COUNTRY_NAMES.contains(test.trim());
        return test.matches(VALIDATION_REGEX) && inCountryList;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Country)) {
            return false;
        }

        Country otherCountry = (Country) other;
        return value.equals(otherCountry.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
