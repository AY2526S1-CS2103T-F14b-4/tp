package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_UNARCHIVED;

import java.util.List;
import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Archives a person in the current list.
 */
public class ArchiveCommand extends Command {

    public static final String COMMAND_WORD = "archive";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Archives the specified person. "
            + "Parameters: index: <index>\n"
            + "Example: " + COMMAND_WORD
            + " 1";

    public static final String MESSAGE_ARCHIVE_SUCCESS = " has been archived";
    public static final String MESSAGE_ALREADY_ARCHIVED = " is already archived";

    private final Index index;

    /**
     * Creates an ArchiveCommand to archive the person.
     */
    public ArchiveCommand(Index index) {
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

        Person personToArchive = lastShownList.get(index.getZeroBased());

        if (personToArchive.getArchivalStatus()) {
            throw new CommandException(personToArchive.getName() + MESSAGE_ALREADY_ARCHIVED);
        } else {
            Person editedPerson = new Person(
                    personToArchive.getName(),
                    personToArchive.getPhone(),
                    personToArchive.getEmail(),
                    personToArchive.getAddress(),
                    personToArchive.getCountry(),
                    personToArchive.getNote(),
                    personToArchive.getTags(),
                    personToArchive.getOffset(),
                    personToArchive.getMetOn(),
                    true);

            model.setPerson(personToArchive, editedPerson);
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_UNARCHIVED);
        }

        return new CommandResult(personToArchive.getName() + MESSAGE_ARCHIVE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ArchiveCommand)
                        && index.equals(((ArchiveCommand) other).index);
    }
}
