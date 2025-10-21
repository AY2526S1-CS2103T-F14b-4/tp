package seedu.address.ui;

import java.sql.Time;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://se-education.org/addressbook-level3/UserGuide.html";
    public static final String HELP_MESSAGE = "Refer to the user guide: " + USERGUIDE_URL;

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    @FXML
    private TableView<CommandEntry> commandTableView;

    @FXML
    private TableView<TimezoneEntry> timezoneTable;

    @FXML
    private ListView<String> countryListView;

    @FXML
    private TableColumn<CommandEntry, String> actionColumn;

    @FXML
    private TableColumn<CommandEntry, String> formatColumn;


    @FXML
    private TableColumn<TimezoneEntry, String> regionColumn;

    @FXML
    private TableColumn<TimezoneEntry, String> offsetColumn;
    @FXML
    private TableView<CountryEntry> countryTableView;

    @FXML
    private TableColumn<CountryEntry, String> countryNameColumn;

    @FXML
    private TableColumn<CountryEntry, String> countryCodeColumn;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpMessage.setText(HELP_MESSAGE);
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    private void initializeTimezones() {
        regionColumn.setCellValueFactory(new PropertyValueFactory<>("region"));
        offsetColumn.setCellValueFactory(new PropertyValueFactory<>("offset"));

        List<TimezoneEntry> zones = getAllZones();
        timezoneTable.getItems().addAll(zones);
    }

    /**
     * Generates a list of all available time zones with their corresponding city/region
     * and UTC offset, sorted by offset (from lowest to highest) and then by city name.
     * <p>
     * The UTC offset is displayed in the format "UTC±HH:MM". The special "Z" offset
     * (representing UTC) is normalized to "+00:00" for display purposes.
     * </p>
     *
     * @return a {@link List} of {@link TimezoneEntry} objects, each representing
     *         a time zone with its city/region and formatted UTC offset.
     */
    private List<TimezoneEntry> getAllZones() {
        List<TimezoneEntry> list = new ArrayList<>();
        ZonedDateTime now = ZonedDateTime.now();

        for(String zoneId : ZoneId.getAvailableZoneIds()) {
            ZoneId zone = ZoneId.of(zoneId);
            ZoneOffset offset = now.withZoneSameInstant(zone).getOffset();

            String offsetString = offset.getId().equals("Z") ? "+00:00" : offset.getId();

            String [] parts = zoneId.split("/");
            String city = parts.length > 1 ? parts[parts.length - 1].replace("_", " ") : zoneId;
            list.add(new TimezoneEntry(city, "UTC" + offsetString));
        }

        list.sort(Comparator
                .comparingInt((TimezoneEntry entry) -> {
                    String offsetStr = entry.getOffset().substring(3);
                    if(offsetStr.equals("Z")) offsetStr = "00:00";
                    String [] hms = offsetStr.split(":");
                    int hours = Integer.parseInt(hms[0]);
                    int minutes = Integer.parseInt(hms[1]);
                    return hours * 60 + (hours < 0 ? -minutes : minutes);
                }).thenComparing(TimezoneEntry::getRegion));
        return list;
    }

    /**
     * Initializes the HelpWindow after its FXML is loaded.
     * Sets up the table columns and populates the command reference table.
     */
    @FXML
    public void initialize() {
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        formatColumn.setCellValueFactory(new PropertyValueFactory<>("format"));

        formatColumn.setCellFactory(column -> {
            return new javafx.scene.control.TableCell<CommandEntry, String>() {
                private final javafx.scene.text.Text text = new javafx.scene.text.Text();

                {
                    text.wrappingWidthProperty().bind(formatColumn.widthProperty().subtract(10));
                    setGraphic(text);
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        text.setText(null);
                    } else {
                        text.setText(item);
                    }
                }
            };
        });

        commandTableView.setFixedCellSize(-1);

        commandTableView.getItems().addAll(
                new CommandEntry("Add", "add name:NAME phone:PHONE email:EMAIL address:ADDRESS "
                        + "country:COUNTRY channel:CHANNEL " + "[note:NOTE] [tag:TAG]…\n + gmt: GMT OFFSET"
                        + "e.g., add name:James Ho phone:22224444 email:jamesho@example.com "
                        + "address:123, Clementi Rd, 1234665 country:Singapore tag:friend tag:colleague + gmt: +08:00"),
                new CommandEntry("Clear", "clear"),
                new CommandEntry("Delete", "delete INDEX\n e.g., delete 3"),
                new CommandEntry("Edit", "edit INDEX [name:NAME] [phone:PHONE] [email:EMAIL] "
                        + "[address:ADDRESS] [country:COUNTRY] [channel:CHANNEL] [tag:TAG]…\n + [gmt: GMT OFFSET]"
                        + "e.g., edit 2 name:James Lee email:jameslee@example.com"),
                new CommandEntry("Add cultural note",
                        "addnote name:NAME note:NOTE\n e.g. addnote name:John note:does not eat beef"),
                new CommandEntry("Find", "find KEYWORD [MORE_KEYWORDS]\n e.g., find James Jake"),
                new CommandEntry("Find tag", "findtag TAG\n e.g. findtag friends"),
                new CommandEntry("Find country", "findcountry COUNTRY\n e.g. findcountry Singapore"),
                new CommandEntry("List", "list"),
                new CommandEntry("Help", "help"));

        countryNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        countryCodeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));

        List<CountryEntry> countries = new ArrayList<>();
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

        for (String regionCode : phoneUtil.getSupportedRegions()) {
            String countryName = new Locale("", regionCode).getDisplayCountry(Locale.ENGLISH);
            if (!countryName.isEmpty()) {
                String phoneCode = "+" + phoneUtil.getCountryCodeForRegion(regionCode);
                countries.add(new CountryEntry(countryName, phoneCode));
            }
        }
        countryNames.sort(String::compareTo);
        countryListView.getItems().addAll(countryNames);
        System.out.println("Added countries: " + countryNames.size());

        initializeTimezones();

        countries.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));
        countryTableView.getItems().addAll(countries);
    }

    /**
     * Shows the help window.
     *
     * @throws IllegalStateException
     *                               <ul>
     *                               <li>
     *                               if this method is called on a thread other than
     *                               the JavaFX Application Thread.
     *                               </li>
     *                               <li>
     *                               if this method is called during animation or
     *                               layout processing.
     *                               </li>
     *                               <li>
     *                               if this method is called on the primary stage.
     *                               </li>
     *                               <li>
     *                               if {@code dialogStage} is already showing.
     *                               </li>
     *                               </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);
    }
}
