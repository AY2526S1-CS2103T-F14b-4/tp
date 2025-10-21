package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.FindCountryCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Country;
import seedu.address.model.person.CountryContainsKeywordPredicate;

/**
 * Parses input arguments and creates a new {@link FindCountryCommand} object.
 *
 * Usage (example):
 *   findcountry Singapore
 * Finds contacts from the country
 *
 */
public class FindCountryCommandParser implements Parser<FindCountryCommand> {

    @Override
    public FindCountryCommand parse(String args) throws ParseException {
        String countryInput = args.trim();
        if (countryInput.equals("")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCountryCommand.MESSAGE_USAGE));
        }
        Country country;
        try {
            country = new Country(countryInput);
        } catch (IllegalArgumentException e) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCountryCommand.MESSAGE_USAGE));
        }
        return new FindCountryCommand(new CountryContainsKeywordPredicate(country));
    }
}
