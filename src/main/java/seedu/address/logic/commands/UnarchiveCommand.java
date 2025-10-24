package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_ARCHIVED;

import java.util.List;
import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Unarchives a person in the current list.
 */
public class UnarchiveCommand extends Command {

    public static final String COMMAND_WORD = "unarchive";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Unarchives the specified person. "
            + "Parameters: index: <index>\n"
            + "Example: " + COMMAND_WORD
            + " 1";

    public static final String MESSAGE_UNARCHIVE_SUCCESS = " has been unarchived";
    public static final String MESSAGE_ALREADY_UNARCHIVED = " is already unarchived";

    private final Index index;

    /**
     * Creates an UnarchiveCommand to unarchive the person.
     */
    public UnarchiveCommand(Index index) {
        requireAllNonNull(index);
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToUnarchive = lastShownList.get(index.getZeroBased());

        if (!personToUnarchive.getArchivalStatus()) {
            throw new CommandException(personToUnarchive.getName() + MESSAGE_ALREADY_UNARCHIVED);
        } else {
            Person editedPerson = new Person(
                    personToUnarchive.getName(),
                    personToUnarchive.getPhone(),
                    personToUnarchive.getEmail(),
                    personToUnarchive.getAddress(),
                    personToUnarchive.getCountry(),
                    personToUnarchive.getNote(),
                    personToUnarchive.getTags(),
                    personToUnarchive.getOffset(),
                    personToUnarchive.getMetOn(),
                    false);

            model.setPerson(personToUnarchive, editedPerson);
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_ARCHIVED);
        }

        return new CommandResult(personToUnarchive.getName() + MESSAGE_UNARCHIVE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof UnarchiveCommand)
                        && index.equals(((UnarchiveCommand) other).index);
    }
}
