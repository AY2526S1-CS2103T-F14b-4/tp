package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated,
 * immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Country country;
    private final MetOn metOn;
    private final Note note;
    private final Set<Tag> tags = new HashSet<>();
    private final CommunicationChannel preferredChannel;
    private final Offset offset;
    private final PreferredLanguage preferredLanguage;
    private final boolean isArchived;

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Country country,
            Note note, CommunicationChannel preferredChannel, Set<Tag> tags, Offset offset, MetOn metOn, boolean isArchived, PreferredLanguage preferredLanguage) {
        requireAllNonNull(name, phone, email, address, note, tags, offset, metOn);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.country = country;
        this.note = note;
        this.preferredChannel = preferredChannel;
        this.tags.addAll(tags);
        this.offset = offset;
        this.preferredLanguage = preferredLanguage;
        this.isArchived = isArchived;
        this.metOn = metOn;

        removeOldCountryTags();

        String countryCode = phone.getCountryCode();
        if (!countryCode.equals("Unknown") && !countryCode.equals("Invalid")) {
            Tag countryTag = new Tag(countryCode);
            this.tags.add(countryTag);
        }
    }

    /**
     * If both note notes and country is included in initialisation.
     */
    public Person(Name name, Phone phone, Email email, Address address,
                  Country country, Note note, Set<Tag> tags, Offset offset, MetOn metOn, boolean isArchived, PreferredLanguage preferredLanguage) {
        requireAllNonNull(name, phone, email, address, note, tags, offset, metOn);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.country = country;
        this.note = note;
        this.preferredChannel = CommunicationChannel.EMAIL;
        this.tags.addAll(tags);
        this.offset = offset;
        this.preferredLanguage = preferredLanguage;
        this.isArchived = isArchived;
        this.metOn = metOn;

        removeOldCountryTags();

        String countryCode = phone.getCountryCode();
        if (!countryCode.equals("Unknown") && !countryCode.equals("Invalid")) {
            Tag countryTag = new Tag(countryCode);
            this.tags.add(countryTag);
        }
    }

    /**
     * Enumeration storing all possible communication channels.
     */
    public enum CommunicationChannel {
        PHONE,
        EMAIL,
        SMS,
        WHATSAPP,
        TELEGRAM
    }

    public CommunicationChannel getPreferredChannel() {
        return preferredChannel;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Note getNote() {
        return note;
    }

    public Country getCountry() {
        return country;
    }

    public PreferredLanguage getPreferredLanguage() {
        return preferredLanguage;
    }


    public boolean getArchivalStatus() {
        return isArchived;
    }

    /**
     * Returns an immutable tag set, which throws
     * {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public Offset getOffset() {
        return offset;
    }

    public MetOn getMetOn() {
        return metOn;
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Removes existing country-related tags.
     * (Assumes tags with names matching country names or flags)
     */
    private void removeOldCountryTags() {
        String[] isoCountries = Locale.getISOCountries();
        Set<String> allCountryNames = new HashSet<>();
        for (String isoCountry : isoCountries) {
            allCountryNames.add(new Locale("", isoCountry).getDisplayCountry());
        }
        this.tags.removeIf(tag -> allCountryNames.stream()
                .anyMatch(country -> country.equalsIgnoreCase(tag.tagName)));
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && country.equals(otherPerson.country)
                && note.equals(otherPerson.note)
                && tags.equals(otherPerson.tags)
                && offset.equals(otherPerson.offset);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, country, note, tags, preferredChannel, offset);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("country", country)
                .add("note", note)
                .add("tags", tags)
                .add("preferredChannel", preferredChannel)
                .add("offset", offset)
                .add("suggestedGreeting", preferredLanguage == null
                        ? "-"
                        : GreetingLibrary.getGreeting(preferredLanguage.toString()))
                .add("metOn", metOn)
                .add("preferredLanguage", preferredLanguage == null ? "-" : preferredLanguage)
                .toString();
    }

}
