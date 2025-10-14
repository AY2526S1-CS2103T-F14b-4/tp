package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COUNTRY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CHANNEL;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Country;
import seedu.address.model.person.Culture;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object as a result
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                PREFIX_ADDRESS, PREFIX_COUNTRY, PREFIX_NOTE, PREFIX_TAG, PREFIX_CHANNEL);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_EMAIL)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                PREFIX_COUNTRY);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        Culture culture = ParserUtil.parseCulture(argMultimap.getValue(PREFIX_NOTE).orElse(""));
        Person.CommunicationChannel preferredChannel = Person.CommunicationChannel.PLATFORM;
        if (argMultimap.getValue(PREFIX_CHANNEL).isPresent()) {
            String channelInput = argMultimap.getValue(PREFIX_CHANNEL).get().toUpperCase();
            switch(channelInput) {
            case "PHONE":
                preferredChannel = Person.CommunicationChannel.PHONE;
                break;
            case "EMAIL":
                preferredChannel = Person.CommunicationChannel.EMAIL;
                break;
            case "SMS":
                preferredChannel = Person.CommunicationChannel.SMS;
                break;
            case "WHATSAPP":
                preferredChannel = Person.CommunicationChannel.WHATSAPP;
                break;
            case "TELEGRAM":
                preferredChannel = Person.CommunicationChannel.TELEGRAM;
                break;
            default:
                throw new ParseException("Invalid communication channel. Choose another channel.");
            }
        }

        Optional<String> countryString = argMultimap.getValue(PREFIX_COUNTRY);
        Optional<Country> countryOptional = Optional.empty();

        if (countryString.isPresent()) {
            countryOptional = ParserUtil.parseCountry(countryString.get());
        }

        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Person.CommunicationChannel finalPreferredChannel = preferredChannel;
        Person person = countryOptional
                .map(country -> new Person(name, phone, email, address, country, culture,
                        tagList, finalPreferredChannel))
                .orElseGet(() -> new Person(name, phone, email, address, culture, tagList, finalPreferredChannel));

        return new AddCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values
     * in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
