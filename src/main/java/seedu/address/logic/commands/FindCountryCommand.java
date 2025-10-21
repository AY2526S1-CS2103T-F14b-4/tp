package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.CountryContainsKeywordPredicate;

/**
 * Finds and lists all persons in address book who comes from the country specified in the argument.
 * Country matching is case sensitive.
 */
public class FindCountryCommand extends Command {

    public static final String COMMAND_WORD = "findcountry";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons who come from "
            + "the specified country (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: COUNTRY \n"
            + "Example: " + COMMAND_WORD + " Singapore";

    private final CountryContainsKeywordPredicate predicate;

    public FindCountryCommand(CountryContainsKeywordPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCountryCommand)) {
            return false;
        }

        FindCountryCommand otherFindCountryCommand = (FindCountryCommand) other;
        return predicate.equals(otherFindCountryCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
