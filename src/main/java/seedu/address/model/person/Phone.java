package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Locale;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

/**
 * Represents a Person's phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPhone(String)}
 */
public class Phone {

    public static final String MESSAGE_CONSTRAINTS = "Phone numbers should be valid international numbers (e.g. +6598765432) "
            + "or contain at least 3 digits if no country code is provided.";

    public static final String VALIDATION_REGEX = "^[+]?([0-9\\-()\\s]){3,}$";
    public final String value;
    public final String countryCode;

    /**
     * Constructs a {@code Phone}.
     *
     * @param phone A valid phone number.
     */
    public Phone(String phone) {
        requireNonNull(phone);
        checkArgument(isValidPhone(phone), MESSAGE_CONSTRAINTS);
        this.value = phone;
        this.countryCode = deriveCountryCode(phone);
    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidPhone(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Uses Google's libphonenumber to parse the phone number and extract the ISO
     * country code.
     */
    private String deriveCountryCode(String phone) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber parsedNumber = phoneUtil.parse(phone, "SG");
            String regionCode = phoneUtil.getRegionCodeForNumber(parsedNumber);
            return regionCode != null ? regionCode : "Unknown";
        } catch (NumberParseException e) {
            return "Invalid";
        }
    }

    private String deriveCountryName(String phone) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber parsedNumber = phoneUtil.parse(phone, "SG");
            String regionCode = phoneUtil.getRegionCodeForNumber(parsedNumber);

            if (regionCode == null || regionCode.isEmpty()) {
                return "Unknown";
            }

            return new Locale("", regionCode).getDisplayCountry(Locale.ENGLISH);
        } catch (NumberParseException e) {
            return "Invalid";
        }
    }

    public String getCountryName() {
        return deriveCountryName(value);
    }

    public String getCountryCode() {
        return countryCode;
    }

    @Override
    public String toString() {
        return value + " (" + countryCode + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Phone otherPhone)) {
            return false;
        }

        return value.equals(otherPhone.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
