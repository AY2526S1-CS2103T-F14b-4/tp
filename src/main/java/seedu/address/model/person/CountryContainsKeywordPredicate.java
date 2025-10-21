package seedu.address.model.person;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Country} matches the country given.
 */
public class CountryContainsKeywordPredicate implements Predicate<Person> {
    private final Country country;

    public CountryContainsKeywordPredicate(Country country) {
        this.country = country;
    }

    @Override
    public boolean test(Person person) {
        return this.country.equals(person.getCountry());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CountryContainsKeywordPredicate)) {
            return false;
        }

        CountryContainsKeywordPredicate otherCountryContainsKeywordPredicate = (CountryContainsKeywordPredicate) other;
        return this.country.equals(otherCountryContainsKeywordPredicate.country);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("country", country).toString();
    }
}
