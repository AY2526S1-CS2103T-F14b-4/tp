package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.JENNY;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Country;
import seedu.address.model.person.CountryContainsKeywordPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCountryCommand}.
 */
public class FindCountryCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        CountryContainsKeywordPredicate firstPredicate =
                new CountryContainsKeywordPredicate(new Country("Singapore"));
        CountryContainsKeywordPredicate secondPredicate =
                new CountryContainsKeywordPredicate(new Country("Malaysia"));

        FindCountryCommand findFirstCommand = new FindCountryCommand(firstPredicate);
        FindCountryCommand findSecondCommand = new FindCountryCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCountryCommand findFirstCommandCopy = new FindCountryCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different countries -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_validCountry_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        CountryContainsKeywordPredicate predicate = preparePredicate("Malaysia");
        FindCountryCommand command = new FindCountryCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_validCountry_personFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        CountryContainsKeywordPredicate predicate = preparePredicate("China");
        FindCountryCommand command = new FindCountryCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(JENNY), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        CountryContainsKeywordPredicate predicate = new CountryContainsKeywordPredicate(new Country("Singapore"));
        FindCountryCommand findCountryCommand = new FindCountryCommand(predicate);
        String expected = FindCountryCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCountryCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code CountryContainsKeywordPredicate}.
     */
    private CountryContainsKeywordPredicate preparePredicate(String userInput) {
        return new CountryContainsKeywordPredicate(new Country(userInput));
    }
}
