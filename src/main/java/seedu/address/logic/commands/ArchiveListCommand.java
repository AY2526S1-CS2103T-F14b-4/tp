package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_ARCHIVED;

import seedu.address.model.Model;

/**
 * Lists all archived persons in the address book to the user.
 */
public class ArchiveListCommand extends Command {

    public static final String COMMAND_WORD = "archivelist";

    public static final String MESSAGE_SUCCESS = "Listed all archived persons";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_ARCHIVED);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
