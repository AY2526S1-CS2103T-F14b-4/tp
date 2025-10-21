package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.FindTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PersonContainsTagsPredicate;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new {@link FindTagCommand} object.
 *
 * Usage (example):
 *   findtag t/friends t/erasmus
 * Matches contacts that have ALL the given tags
 *
 */
public class FindTagCommandParser implements Parser<FindTagCommand> {

    @Override
    public FindTagCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindTagCommand.MESSAGE_USAGE));
        }

        String[] tagKeywords = trimmedArgs.split("\\s+");
        List<Tag> tagList = new ArrayList<>();
        for (String s : tagKeywords) {
            tagList.add(new Tag(s));
        }

        return new FindTagCommand(new PersonContainsTagsPredicate(tagList));
    }
}
