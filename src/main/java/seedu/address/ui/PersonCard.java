package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;
import seedu.address.model.util.GreetingMap;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved
     * keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The
     *      issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label country;
    @FXML
    private FlowPane tags;
    @FXML
    private Label note;
    @FXML
    private Label channel;
    @FXML
    private Label offset;
    @FXML
    private Label preferredLanguage;
    @FXML
    private Label metOn;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to
     * display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        name.getStyleClass().add("bold-label");
        phone.setText(person.getPhone().value);
        phone.getStyleClass().add("bold-label");
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);

        if (!person.getNote().value.equals("")) {
            note.setVisible(true);
            note.setText(person.getNote().value);
        } else {
            note.setVisible(false);
            note.setManaged(false);
        }

        if (person.getPreferredChannel() != null) {
            channel.setVisible(true);
            channel.setText("Preferred communication channel: " + person.getPreferredChannel().name());
        } else {
            channel.setVisible(false);
            channel.setManaged(false);
        }

        if (!person.getCountry().value.equals("")) {
            country.setVisible(true);
            country.setText(person.getCountry().value);
        } else {
            country.setVisible(false);
            country.setManaged(false);
        }

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> {
                    Label tagLabel = new Label(tag.tagName);

                    // Highlight only if it matches the person's country value (case-insensitive)
                    if (!person.getCountry().value.isEmpty()
                            && tag.tagName.equalsIgnoreCase(person.getCountry().value)) {
                        tagLabel.getStyleClass().add("country-tag");
                    } else {
                        tagLabel.getStyleClass().add("normal-tag");
                    }

                    tags.getChildren().add(tagLabel);
                });

        if (person.getOffset() != null) {
            offset.setVisible(true);
            offset.setText("OFFSET : " + person.getOffset());
        } else {
            offset.setVisible(false);
            offset.setManaged(false);
        }

        if (person.getPreferredLanguage() != null) {
            preferredLanguage.setVisible(true);
            String lang = person.getPreferredLanguage().getPreferredLanguage();
            String greeting = GreetingMap.getGreeting(lang);
            preferredLanguage.setText("Language: " + lang + " (" + greeting + ")");
        } else {
            preferredLanguage.setVisible(false);
            preferredLanguage.setManaged(false);
        }


        if (person.getMetOn() != null) {
            metOn.setVisible(true);
            metOn.setText("Met on: " + person.getMetOn());
        } else {
            metOn.setVisible(false);
            metOn.setManaged(false);
        }
    }
}
