package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCountryCommand;
import seedu.address.model.person.Country;
import seedu.address.model.person.CountryContainsKeywordPredicate;

public class FindCountryCommandParserTest {

    private FindCountryCommandParser parser = new FindCountryCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCountryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidCountry_throwsParseException() {
        assertParseFailure(parser, "hello",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCountryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCountryCommand() {
        // no leading and trailing whitespaces
        FindCountryCommand expectedFindCountryCommand =
                new FindCountryCommand(new CountryContainsKeywordPredicate(new Country("Singapore")));
        assertParseSuccess(parser, "Singapore", expectedFindCountryCommand);
    }
}
