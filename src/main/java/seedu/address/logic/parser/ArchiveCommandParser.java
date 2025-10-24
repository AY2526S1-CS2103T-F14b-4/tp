package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ArchiveCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code ArchiveCommand} object.
 *
 * <p>
 * Expected format: {@code archive index: &lt;name&gt;} // TO CHANGE
 * </p>
 */
public class ArchiveCommandParser implements Parser<ArchiveCommand> {

    @Override
    public ArchiveCommand parse(String args) throws ParseException {
        requireNonNull(args);
        Index index;

        try {
            index = ParserUtil.parseIndex(args.trim());
            requireNonNull(index);
            return new ArchiveCommand(index);
        } catch (ParseException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ArchiveCommand.MESSAGE_USAGE));
        }
    }
}
