package seedu.address.ui;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2526s1-cs2103t-f14b-4.github.io/tp/UserGuide.html";
    public static final String HELP_MESSAGE = "Refer to the user guide: " + USERGUIDE_URL;

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    @FXML
    private TabPane tabPane;

    @FXML
    private TableView<CommandEntry> commandTableView;

    @FXML
    private TableColumn<CommandEntry, String> actionColumn;

    @FXML
    private TableColumn<CommandEntry, String> formatColumn;

    @FXML
    private TableView<CountryEntry> countryTableView;

    @FXML
    private TableColumn<CountryEntry, String> countryNameColumn;

    @FXML
    private TableColumn<CountryEntry, String> countryCodeColumn;

    @FXML
    private TableView<TimezoneEntry> timezoneTable;

    @FXML
    private TextField timezoneSearchField;

    @FXML
    private TableColumn<TimezoneEntry, String> regionColumn;

    @FXML
    private TableColumn<TimezoneEntry, String> offsetColumn;

    @FXML
    private TextField countrySearchField;

    @FXML
    private TableView<LanguageEntry> languageTableView;

    @FXML
    private TableColumn<LanguageEntry, String> languageNameColumn;

    @FXML
    private TableColumn<LanguageEntry, String> languageGreetingsColumn;

    @FXML
    private TextField languageSearchField;

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

        FilteredList<TimezoneEntry> filteredZones = new FilteredList<>(FXCollections.observableArrayList(zones),
                p -> true);
        timezoneTable.setItems(filteredZones);

        timezoneSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String lower = newValue == null ? "" : newValue.toLowerCase();
            filteredZones.setPredicate(zone -> zone.getRegion().toLowerCase().contains(lower)
                    || zone.getOffset().toLowerCase().contains(lower));
        });
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

        for (String zoneId : ZoneId.getAvailableZoneIds()) {
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
                    if (offsetStr.equals("Z")) {
                        offsetStr = "00:00";
                    }
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
                new CommandEntry("Help", "help"),
                new CommandEntry("Add", "add name:NAME phone:PHONE email:EMAIL address:ADDRESS offset:OFFSET "
                        + "[country:COUNTRY] [organisation:ORGANISATION] [event:EVENT] [channel:CHANNEL] "
                        + "[language:LANGUAGE] [note:NOTE] [tag:TAG]...\n"
                        + "e.g. add name:James Ho phone:22224444 email:jamesho@example.com "
                        + "address:123, Clementi Rd, 1234665  offset:+08:00 country:Singapore language:English "
                        + "tag:friend tag:colleague"),
                new CommandEntry("List", "list"),
                new CommandEntry("Edit", "edit INDEX [name:NAME] [phone:PHONE] [email:EMAIL] "
                        + "[address:ADDRESS] [offset: OFFSET] [country:COUNTRY] [organisation:ORGANISATION] "
                        + "[event:EVENT] [channel:CHANNEL] [language:LANGUAGE] [tag:TAG]...\n"
                        + "e.g. edit 2 name:James Lee email:jameslee@example.com"),
                new CommandEntry("Delete", "delete INDEX\n e.g. delete 3"),
                new CommandEntry("Find", "find KEYWORD [MORE_KEYWORDS]\n e.g. find James Jake"),
                new CommandEntry("Find tag", "findtag TAG\n e.g. findtag friends"),
                new CommandEntry("Find country", "findcountry COUNTRY\n e.g. findcountry Singapore"),
                new CommandEntry("Archive", "archive INDEX"),
                new CommandEntry("Unarchive", "unarchive INDEX"),
                new CommandEntry("Archive list", "archivelist"),
                new CommandEntry("Sort by country", "sortcountry"),
                new CommandEntry("Sort by name", "sortname"),
                new CommandEntry("Sort by date added", "sortdate"),
                new CommandEntry("Clear", "clear"),
                new CommandEntry("Exit", "exit"));

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

        countries.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));
        FilteredList<CountryEntry> filteredCountries = new FilteredList<>(FXCollections.observableArrayList(countries),
                p -> true);
        countryTableView.setItems(filteredCountries);

        countrySearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String lower = newValue.toLowerCase();
            filteredCountries.setPredicate(country -> {
                if (lower == null || lower.isBlank()) {
                    return true;
                }
                return country.getName().toLowerCase().contains(lower)
                        || country.getCode().toLowerCase().contains(lower);
            });
        });

        initializeTimezones();

        languageNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        languageGreetingsColumn.setCellValueFactory(new PropertyValueFactory<>("greeting"));

// Wrap long text
        languageGreetingsColumn.setCellFactory(col -> new TableCell<LanguageEntry, String>() {
            private final javafx.scene.text.Text text = new javafx.scene.text.Text();
            {
                text.wrappingWidthProperty().bind(languageGreetingsColumn.widthProperty().subtract(15));
                setGraphic(text);
                setPrefHeight(USE_COMPUTED_SIZE);
            }

            @Override
            protected void updateItem(String hello, boolean empty) {
                super.updateItem(hello, empty);
                text.setText((empty || hello == null) ? null : hello);
            }
        });

        List<LanguageEntry> languages = new ArrayList<>();

        languages.add(new LanguageEntry("Abkhaz", "Бзиа збаша"));
        languages.add(new LanguageEntry("Adyghe", "Фэсапщы"));
        languages.add(new LanguageEntry("Afrikaans", "Hallo"));
        languages.add(new LanguageEntry("Akkadian", "šulmu"));
        languages.add(new LanguageEntry("Aklan", "Kamusta"));
        languages.add(new LanguageEntry("Albanian Gh", "Mir-dita"));
        languages.add(new LanguageEntry("Albanian To", "Tungjatjeta"));
        languages.add(new LanguageEntry("Aleut", "Aang"));
        languages.add(new LanguageEntry("Alsatian", "Hallo"));
        languages.add(new LanguageEntry("Altay", "Јакшы"));
        languages.add(new LanguageEntry("Amharic", "ሰላም።"));
        languages.add(new LanguageEntry("Arabic", "مرحبا"));
        languages.add(new LanguageEntry("Aragonese", "Ola"));
        languages.add(new LanguageEntry("Aramaic", "Shl'am lak"));
        languages.add(new LanguageEntry("Arapaho", "Héébee"));
        languages.add(new LanguageEntry("Armenian Eastern", "բարև"));
        languages.add(new LanguageEntry("Armenian Western", "Բարեւ:"));
        languages.add(new LanguageEntry("Aromanian", "Salut"));
        languages.add(new LanguageEntry("Arrernte", "Werte"));
        languages.add(new LanguageEntry("Assamese", "নমস্কাৰ"));
        languages.add(new LanguageEntry("Asturian", "Hola"));
        languages.add(new LanguageEntry("Atikamekw", "Kwei"));
        languages.add(new LanguageEntry("Aymara", "Aski urükipana"));
        languages.add(new LanguageEntry("Azerbaijani", "Salam"));
        languages.add(new LanguageEntry("Balinese", "Swastiastu"));
        languages.add(new LanguageEntry("Bambara", "I ni ce"));
        languages.add(new LanguageEntry("Bamum", "Me sha'ashe"));
        languages.add(new LanguageEntry("Barbareno", "Haku'"));
        languages.add(new LanguageEntry("Bashkir", "сәләм"));
        languages.add(new LanguageEntry("Basque", "Kaixo"));
        languages.add(new LanguageEntry("Bassa", "M̀ mɔ́ínǹ"));
        languages.add(new LanguageEntry("Batak Toba", "Horas"));
        languages.add(new LanguageEntry("Batsbi", "მარშიხ ვალ"));
        languages.add(new LanguageEntry("Bavarian", "Servus"));
        languages.add(new LanguageEntry("Belarusian", "Вітаю"));
        languages.add(new LanguageEntry("Bemba", "Shani"));
        languages.add(new LanguageEntry("Bengali", "নমস্কার"));
        languages.add(new LanguageEntry("Bete", "Ayo"));
        languages.add(new LanguageEntry("Bhojpuri", "प्रणाम"));
        languages.add(new LanguageEntry("Biatah", "Kudu?"));
        languages.add(new LanguageEntry("Bikol", "Hello"));
        languages.add(new LanguageEntry("Bislama", "Halo"));
        languages.add(new LanguageEntry("Blaan", "Sàlamàt"));
        languages.add(new LanguageEntry("Bosnian", "Dobar dan"));
        languages.add(new LanguageEntry("Breton", "Salud"));
        languages.add(new LanguageEntry("Bulgarian", "Здравейте"));
        languages.add(new LanguageEntry("Burmese", "မဂႆလာပၝ"));
        languages.add(new LanguageEntry("Cape Verdean Creole", "Olá"));
        languages.add(new LanguageEntry("Catalan", "Hola"));
        languages.add(new LanguageEntry("Cebuano", "Hello"));
        languages.add(new LanguageEntry("Central Dusun", "Kopisanangan"));
        languages.add(new LanguageEntry("Chabacano Zamboanga", "Que tal?"));
        languages.add(new LanguageEntry("Chabacano Cavite", "Ola"));
        languages.add(new LanguageEntry("Chamorro Guam", "Håfa ådai"));
        languages.add(new LanguageEntry("Chamorro North", "Hafa Adai"));
        languages.add(new LanguageEntry("Chechen", "Салам"));
        languages.add(new LanguageEntry("Cherokee", "ᎣᏏᏲ"));
        languages.add(new LanguageEntry("Chichewa", "Moni"));
        languages.add(new LanguageEntry("Chinese Cantonese", "你好"));
        languages.add(new LanguageEntry("Chinese Hakka", "你好"));
        languages.add(new LanguageEntry("Chinese Mandarin", "你好"));
        languages.add(new LanguageEntry("Mandarin", "你好"));
        languages.add(new LanguageEntry("Chinese Shanghainese", "侬好"));
        languages.add(new LanguageEntry("Chinese Taiwanese", "你好"));
        languages.add(new LanguageEntry("Chinese Teochew", "汝好"));
        languages.add(new LanguageEntry("Chinese", "你好"));
        languages.add(new LanguageEntry("Chipewyan", "ʔédlánet'é"));
        languages.add(new LanguageEntry("Choctaw", "Halito"));
        languages.add(new LanguageEntry("Chuukese", "Ran annim"));
        languages.add(new LanguageEntry("Chuvash", "Салам!"));
        languages.add(new LanguageEntry("Cimbrian", "Guuten takh!"));
        languages.add(new LanguageEntry("Coastal Kadazan", "Kopivosian"));
        languages.add(new LanguageEntry("Comanche", "Marʉ́awe"));
        languages.add(new LanguageEntry("Cornish", "Dydh da"));
        languages.add(new LanguageEntry("Corsican", "Salute"));
        languages.add(new LanguageEntry("Cree", "ᑖᓂᓯ"));
        languages.add(new LanguageEntry("Croatian", "Bok"));
        languages.add(new LanguageEntry("Crow", "Sho'daache"));
        languages.add(new LanguageEntry("Cumbric", "Didh da"));
        languages.add(new LanguageEntry("Cupeño", "Míyaxwe"));
        languages.add(new LanguageEntry("Cuyonon", "Komosta?"));
        languages.add(new LanguageEntry("Czech", "Ahoj"));
        languages.add(new LanguageEntry("Danish", "Hej"));
        languages.add(new LanguageEntry("Dari", "As-salâmo 'alaykom"));
        languages.add(new LanguageEntry("Dholuo", "Misawa"));
        languages.add(new LanguageEntry("Ditidaht", "ʔux̣ʷaƛak"));
        languages.add(new LanguageEntry("Duala", "Mônè"));
        languages.add(new LanguageEntry("Dutch", "Hallo"));
        languages.add(new LanguageEntry("Dzongkha", "སྐུ་གཟུགས་བཟང་པོ།"));
        languages.add(new LanguageEntry("Efik", "Mọkọm"));
        languages.add(new LanguageEntry("Elfdalian", "Häj ą̊ dig!"));
        languages.add(new LanguageEntry("English", "Hello"));
        languages.add(new LanguageEntry("Estonian", "Tere"));
        languages.add(new LanguageEntry("Extremaduran", "Ola!"));
        languages.add(new LanguageEntry("Faroese", "Hallo"));
        languages.add(new LanguageEntry("Fijian", "Bula"));
        languages.add(new LanguageEntry("Finnish", "Terve"));
        languages.add(new LanguageEntry("French", "Bonjour"));
        languages.add(new LanguageEntry("Frisian North", "Moin"));
        languages.add(new LanguageEntry("Frisian Saterland", "Gouden Dai"));
        languages.add(new LanguageEntry("Frisian West", "Hoi"));
        languages.add(new LanguageEntry("Friulian", "Mandi"));
        languages.add(new LanguageEntry("Galician", "Ola"));
        languages.add(new LanguageEntry("Galo", "Aldure"));
        languages.add(new LanguageEntry("Gallo", "Bonjou"));
        languages.add(new LanguageEntry("Garhwali", "सिवासौँळी"));
        languages.add(new LanguageEntry("Garifuna", "Buiti binafi"));
        languages.add(new LanguageEntry("Georgian", "გამარჯობა"));
        languages.add(new LanguageEntry("German", "Hallo"));
        languages.add(new LanguageEntry("Greek Ancient", "Χαῖρε!"));
        languages.add(new LanguageEntry("Greek", "Γειά σας"));
        languages.add(new LanguageEntry("Greenlandic", "Aluu"));
        languages.add(new LanguageEntry("Guernesiais", "Warro"));
        languages.add(new LanguageEntry("Gujarati", "નમસ્તે"));
        languages.add(new LanguageEntry("Haitian Creole", "Bonjou"));
        languages.add(new LanguageEntry("Hausa", "Sannu"));
        languages.add(new LanguageEntry("Hawaiian", "Aloha"));
        languages.add(new LanguageEntry("Hebrew", "שלום"));
        languages.add(new LanguageEntry("Herero", "Tjike?"));
        languages.add(new LanguageEntry("Hindi", "नमस्ते"));
        languages.add(new LanguageEntry("Hmong White", "Nyob zoo"));
        languages.add(new LanguageEntry("Hopi", "Ahéé'"));
        languages.add(new LanguageEntry("Hungarian", "Jó napot kívánok"));
        languages.add(new LanguageEntry("Icelandic", "Halló"));
        languages.add(new LanguageEntry("Igbo", "Ndeewo"));
        languages.add(new LanguageEntry("Iloko", "Kablaaw"));
        languages.add(new LanguageEntry("Inari Saami", "Tiervâ"));
        languages.add(new LanguageEntry("Indonesian", "Hi"));
        languages.add(new LanguageEntry("Inuktitut", "ᐊᐃ"));
        languages.add(new LanguageEntry("Iñupiaq", "Haluu"));
        languages.add(new LanguageEntry("Irish", "Dia dhuit"));
        languages.add(new LanguageEntry("Italian", "Ciao"));
        languages.add(new LanguageEntry("Ivilyuat Cahuilla", "Míyaxwen"));
        languages.add(new LanguageEntry("Jamaican", "Ello"));
        languages.add(new LanguageEntry("Japanese", "今日は"));
        languages.add(new LanguageEntry("Javanese", "ꦲꦭꦺꦴ"));
        languages.add(new LanguageEntry("Jenesch", "Puiznu"));
        languages.add(new LanguageEntry("Jèrriais", "Salut"));
        languages.add(new LanguageEntry("Jutish", "Godaw"));
        languages.add(new LanguageEntry("Kabyle", "ⴰⵣⵓⵍ"));
        languages.add(new LanguageEntry("Kalmyk", "Мендвт"));
        languages.add(new LanguageEntry("Kam", "Tọi"));
        languages.add(new LanguageEntry("Kannada", "ನಮಸ್ತೆ"));
        languages.add(new LanguageEntry("Kaqchikel", "Xsaqär"));
        languages.add(new LanguageEntry("Karuk", "Ayukîi"));
        languages.add(new LanguageEntry("Kashubian", "Witéj"));
        languages.add(new LanguageEntry("Kashmiri", "Halo"));
        languages.add(new LanguageEntry("Kawaiisu", "Hagare'enaam"));
        languages.add(new LanguageEntry("Kazakh", "Сәлем!"));
        languages.add(new LanguageEntry("Khmer", "ជំរាបសួរ"));
        languages.add(new LanguageEntry("Khoekhoe", "Halau"));
        languages.add(new LanguageEntry("Kikuyu", "wĩmwega"));
        languages.add(new LanguageEntry("Kinyarwanda", "Muraho"));
        languages.add(new LanguageEntry("Kiribati", "Ko na mauri"));
        languages.add(new LanguageEntry("Koasati", "Chikaano?"));
        languages.add(new LanguageEntry("Korean", "안녕하세요"));
        languages.add(new LanguageEntry("Kpelle", "Ya tûa"));
        languages.add(new LanguageEntry("Kumeyaay", "Haawka"));
        languages.add(new LanguageEntry("!kung San", "!kao"));
        languages.add(new LanguageEntry("Kurdish Kurmanji", "Rojbash"));
        languages.add(new LanguageEntry("Kurdish Sorani", "Sillaw"));
        languages.add(new LanguageEntry("Kven", "Hei"));
        languages.add(new LanguageEntry("Kwangali", "Morokeni"));
        languages.add(new LanguageEntry("Kyrgyz", "Салам!"));
        languages.add(new LanguageEntry("Ladino", "Buenos diyas"));
        languages.add(new LanguageEntry("Lakota Sioux", "Hau"));
        languages.add(new LanguageEntry("Lao", "ສະບາຍດີ"));
        languages.add(new LanguageEntry("Latgalian", "Vasaļs"));
        languages.add(new LanguageEntry("Latin", "Salve"));
        languages.add(new LanguageEntry("Latvian", "Sveiki"));
        languages.add(new LanguageEntry("Laz", "გეგაჯგინას"));
        languages.add(new LanguageEntry("Lengola", "Ishamba"));
        languages.add(new LanguageEntry("Lezgi", "Салам алейкум"));
        languages.add(new LanguageEntry("Limburgish", "Hallo"));
        languages.add(new LanguageEntry("Lingala", "Mbote"));
        languages.add(new LanguageEntry("Lithuanian", "Labas"));
        languages.add(new LanguageEntry("Livvi Karelian", "Terveh"));
        languages.add(new LanguageEntry("Low Saxon", "Moin"));
        languages.add(new LanguageEntry("Lozi", "Lumela"));
        languages.add(new LanguageEntry("Luganda", "Gyebale ko"));
        languages.add(new LanguageEntry("Lule Sami", "Buoris"));
        languages.add(new LanguageEntry("Luiseño", "Mííyu"));
        languages.add(new LanguageEntry("Luxembourgish", "Moien"));
        languages.add(new LanguageEntry("Macedonian", "Здраво"));
        languages.add(new LanguageEntry("Magahi", "परनाम"));
        languages.add(new LanguageEntry("Maithili", "प्रनाम"));
        languages.add(new LanguageEntry("Malagasy", "Manao ahoana"));
        languages.add(new LanguageEntry("Malay", "Selamat pagi"));
        languages.add(new LanguageEntry("Malayalam", "നമസ്തേ"));
        languages.add(new LanguageEntry("Mam", "Jeeka"));
        languages.add(new LanguageEntry("Manx", "Moghrey mie"));
        languages.add(new LanguageEntry("Maldivian", "Assalaamu Alaikum"));
        languages.add(new LanguageEntry("Maltese", "Ħello"));
        languages.add(new LanguageEntry("Maori", "Kia ora"));
        languages.add(new LanguageEntry("Mapuche", "Mari mari"));
        languages.add(new LanguageEntry("Marathi", "नमस्कार"));
        languages.add(new LanguageEntry("Marshallese", "Io̧kwe"));
        languages.add(new LanguageEntry("Mauritian Creole", "Bonzur"));
        languages.add(new LanguageEntry("Meänkieli", "Terve"));
        languages.add(new LanguageEntry("Míkmaq", "Gwe'"));
        languages.add(new LanguageEntry("Mirandese", "Oulá"));
        languages.add(new LanguageEntry("Mising", "Aydun?"));
        languages.add(new LanguageEntry("Mon", "ဟာဲ"));
        languages.add(new LanguageEntry("Monégasque", "Ciau"));
        languages.add(new LanguageEntry("Mongolian", "Сайн уу?"));
        languages.add(new LanguageEntry("Mono", "Manahúú"));
        languages.add(new LanguageEntry("Mossi", "Ne y windiga"));
        languages.add(new LanguageEntry("Muscogee", "Hę̄r's cē"));
        languages.add(new LanguageEntry("Nahuatl", "Niltze"));
        languages.add(new LanguageEntry("Nauruan", "Ekamowir omo"));
        languages.add(new LanguageEntry("Navajo", "Yá'át'ééh"));
        languages.add(new LanguageEntry("Nawdm", "Seenŋb"));
        languages.add(new LanguageEntry("Newari", "ज्वजलपा"));
        languages.add(new LanguageEntry("Ngoni", "Hujambo"));
        languages.add(new LanguageEntry("Sakubona", "Sawubona"));
        languages.add(new LanguageEntry("Nkore", "Oraire ota?"));
        languages.add(new LanguageEntry("Ndebele Northern", "Salibonani"));
        languages.add(new LanguageEntry("Ndebele Southern", "Lotjhani"));
        languages.add(new LanguageEntry("Nepali", "नमस्ते"));
        languages.add(new LanguageEntry("Niuean", "Fakaalofa atu"));
        languages.add(new LanguageEntry("Nogai", "Salam"));
        languages.add(new LanguageEntry("Norwegian Bokmal", "Hei"));
        languages.add(new LanguageEntry("Norwegian Nynorsk", "Hei"));
        languages.add(new LanguageEntry("Norwegian Railway", "Hallo"));
        languages.add(new LanguageEntry("Nyanja", "Moni"));
        languages.add(new LanguageEntry("Occitan", "Adieu"));
        languages.add(new LanguageEntry("Ojibwe", "Boozhoo"));
        languages.add(new LanguageEntry("Okinawan", "はいさい"));
        languages.add(new LanguageEntry("Old English", "Hāl"));
        languages.add(new LanguageEntry("Oromo", "Akkam"));
        languages.add(new LanguageEntry("Ossetian", "Дӕ здравӕй"));
        languages.add(new LanguageEntry("Palauan", "Alii"));
        languages.add(new LanguageEntry("Pali", "Namo"));
        languages.add(new LanguageEntry("Pangasinan", "Kumusta"));
        languages.add(new LanguageEntry("Papiamento", "Halo"));
        languages.add(new LanguageEntry("Pashto", "سلام"));
        languages.add(new LanguageEntry("Persian", "سلام"));
        languages.add(new LanguageEntry("Pidgin", "Olà"));
        languages.add(new LanguageEntry("Polish", "Cześć"));
        languages.add(new LanguageEntry("Portuguese", "Olá"));
        languages.add(new LanguageEntry("Punjabi", "ਸਤ ਸ੍ਰੀ ਅਕਾਲ"));
        languages.add(new LanguageEntry("Quechua", "Rimaykullayki"));
        languages.add(new LanguageEntry("Romanian", "Salut"));
        languages.add(new LanguageEntry("Russian", "Привет"));
        languages.add(new LanguageEntry("Samoan", "Talofa"));
        languages.add(new LanguageEntry("Sanskrit", "नमः"));
        languages.add(new LanguageEntry("Scottish Gaelic", "Halò"));
        languages.add(new LanguageEntry("Serbian", "Здраво"));
        languages.add(new LanguageEntry("Serer", "Nanga def"));
        languages.add(new LanguageEntry("Shona", "Mhoro"));
        languages.add(new LanguageEntry("Sichuan Yi", "ꀉꑤ"));
        languages.add(new LanguageEntry("Sicilian", "Ciau"));
        languages.add(new LanguageEntry("Sindhi", "سلام"));
        languages.add(new LanguageEntry("Sinhalese", "ආයුබෝවන්"));
        languages.add(new LanguageEntry("Slovak", "Ahoj"));
        languages.add(new LanguageEntry("Slovene", "Živjo"));
        languages.add(new LanguageEntry("Somali", "Iska warran"));
        languages.add(new LanguageEntry("Sotho", "Dumelang"));
        languages.add(new LanguageEntry("Spanish", "Hola"));
        languages.add(new LanguageEntry("Sundanese", "Halo"));
        languages.add(new LanguageEntry("Swahili", "Hujambo"));
        languages.add(new LanguageEntry("Swati", "Sawubona"));
        languages.add(new LanguageEntry("Swedish", "Hej"));
        languages.add(new LanguageEntry("Syriac", "Shlomo"));
        languages.add(new LanguageEntry("Tahitian", "Ia ora na"));
        languages.add(new LanguageEntry("Tajik", "Салом"));
        languages.add(new LanguageEntry("Tamil", "வணக்கம்"));
        languages.add(new LanguageEntry("Tatar", "Исәнмесез"));
        languages.add(new LanguageEntry("Telugu", "నమస్కారం"));
        languages.add(new LanguageEntry("Thai", "สวัสดี"));
        languages.add(new LanguageEntry("Tibetan", "བཀྲ་ཤིས་"));
        languages.add(new LanguageEntry("Tigre", "Selam"));
        languages.add(new LanguageEntry("Tigrinya", "ሰላም"));
        languages.add(new LanguageEntry("Tok Pisin", "Gude"));
        languages.add(new LanguageEntry("Tongan", "Mālō e lelei"));
        languages.add(new LanguageEntry("Tsonga", "Xewani"));
        languages.add(new LanguageEntry("Tswana", "Dumela"));
        languages.add(new LanguageEntry("Turkish", "Merhaba"));
        languages.add(new LanguageEntry("Turkmen", "Salam"));
        languages.add(new LanguageEntry("Tuvan", "Салам"));
        languages.add(new LanguageEntry("Ukrainian", "Привіт"));
        languages.add(new LanguageEntry("Urdu", "السلام علیکم"));
        languages.add(new LanguageEntry("Uyghur", "ياخشىمۇسىز"));
        languages.add(new LanguageEntry("Uzbek", "Salom"));
        languages.add(new LanguageEntry("Valencian", "Hola"));
        languages.add(new LanguageEntry("Venda", "Ndaa"));
        languages.add(new LanguageEntry("Vietnamese", "Xin chào"));
        languages.add(new LanguageEntry("Volapuk", "Oläf"));
        languages.add(new LanguageEntry("Votic", "Tereh"));
        languages.add(new LanguageEntry("Walser", "Grüezi"));
        languages.add(new LanguageEntry("Waray", "Maupay"));
        languages.add(new LanguageEntry("Welsh", "Helo"));
        languages.add(new LanguageEntry("Western Frisian", "Hoi"));
        languages.add(new LanguageEntry("Wintu", "Gününü"));
        languages.add(new LanguageEntry("Wolaytta", "Aada"));
        languages.add(new LanguageEntry("Wolof", "Naka nga def"));
        languages.add(new LanguageEntry("Xhosa", "Molo"));
        languages.add(new LanguageEntry("Yiddish", "העלא"));
        languages.add(new LanguageEntry("Yoruba", "Bawo"));
        languages.add(new LanguageEntry("Zaza", "Merheba"));
        languages.add(new LanguageEntry("Zhuang", "Naeuz"));
        languages.add(new LanguageEntry("Zulu", "Sawubona"));

// Sort alphabetically
        languages.sort(Comparator.comparing(LanguageEntry::getName));

// FilteredList for search
        FilteredList<LanguageEntry> filteredLanguages = new FilteredList<>(FXCollections.observableArrayList(languages), p -> true);
        languageTableView.setItems(filteredLanguages);

        languageSearchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String lower = newVal == null ? "" : newVal.toLowerCase();
            filteredLanguages.setPredicate(lang ->
                    lang.getName().toLowerCase().contains(lower) ||
                            lang.getGreeting().toLowerCase().contains(lower));
        });

        languageTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tabPane.getTabs().forEach(tab -> tab.setClosable(false));
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
