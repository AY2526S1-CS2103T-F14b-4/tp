package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Collections;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.OrganisationContainsKeywordPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindOrganisationCommand}.
 * Organisation matching is **case-sensitive**.
 */
public class FindOrganisationCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        OrganisationContainsKeywordPredicate firstPredicate =
            new OrganisationContainsKeywordPredicate("NUS");
        OrganisationContainsKeywordPredicate secondPredicate =
            new OrganisationContainsKeywordPredicate("NTU");

        FindOrganisationCommand findFirstCommand = new FindOrganisationCommand(firstPredicate);
        FindOrganisationCommand findSecondCommand = new FindOrganisationCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindOrganisationCommand findFirstCommandCopy = new FindOrganisationCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different organisations -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_validOrganisation_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        OrganisationContainsKeywordPredicate predicate = preparePredicate("NonExistentOrg");
        FindOrganisationCommand command = new FindOrganisationCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_validOrganisation_personFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        OrganisationContainsKeywordPredicate predicate = preparePredicate("NUS"); // exact case
        FindOrganisationCommand command = new FindOrganisationCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(BENSON), model.getFilteredPersonList());
    }

    @Test
    public void execute_wrongCaseOrganisation_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        OrganisationContainsKeywordPredicate predicate = preparePredicate("nus"); // wrong case
        FindOrganisationCommand command = new FindOrganisationCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        OrganisationContainsKeywordPredicate predicate = preparePredicate("NUS");
        FindOrganisationCommand findOrgCommand = new FindOrganisationCommand(predicate);
        String expected = FindOrganisationCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findOrgCommand.toString());
    }

    /**
     * Converts {@code userInput} into a {@code OrganisationContainsKeywordPredicate}.
     *
     * @param userInput Organisation keyword string.
     * @return OrganisationContainsKeywordPredicate containing the given keyword.
     */
    private OrganisationContainsKeywordPredicate preparePredicate(String userInput) {
        return new OrganisationContainsKeywordPredicate(userInput);
    }
}
