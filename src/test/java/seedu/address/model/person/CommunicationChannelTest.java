package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CommunicationChannelTest {

    // Test enum constants
    @Test
    public void testEnumValuesExist() {
        for (Person.CommunicationChannel channel : Person.CommunicationChannel.values()) {
            assertNotNull(channel, "Enum constant should not be null");
        }
    }

    // Test conversion from String
    @Test
    public void testValueOfStringConversion() {
        assertEquals(Person.CommunicationChannel.EMAIL, Person.CommunicationChannel.valueOf("EMAIL"));
        assertEquals(Person.CommunicationChannel.SMS, Person.CommunicationChannel.valueOf("SMS"));
        assertEquals(Person.CommunicationChannel.WHATSAPP, Person.CommunicationChannel.valueOf("WHATSAPP"));

        // Invalid value should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            Person.CommunicationChannel.valueOf("INVALID_CHANNEL");
        });
    }

    // Test toString() outputs
    @Test
    public void testToStringOutput() {
        assertEquals("PHONE", Person.CommunicationChannel.PHONE.toString());
        assertEquals("EMAIL", Person.CommunicationChannel.EMAIL.toString());
        assertEquals("SMS", Person.CommunicationChannel.SMS.toString());
        assertEquals("WHATSAPP", Person.CommunicationChannel.WHATSAPP.toString());
        assertEquals("TELEGRAM", Person.CommunicationChannel.TELEGRAM.toString());
    }

    // Test switch/case logic using enum
    @Test
    public void testSwitchLogic() {
        Person.CommunicationChannel channel = Person.CommunicationChannel.SMS;

        String message;
        switch(channel) {
        case PHONE -> message = "Call user";
        case EMAIL -> message = "Send email";
        case SMS -> message = "Send text";
        case WHATSAPP -> message = "Send WhatsApp message";
        case TELEGRAM -> message = "Send Telegram message";
        default -> message = "Unknown channel";
        }

        assertEquals("Send text", message);
    }

    // Test all channels in a loop
    @Test
    public void testAllChannelsLoop() {
        for (Person.CommunicationChannel channel : Person.CommunicationChannel.values()) {
            assertTrue(channel.name().length() > 0, "Channel name should not be empty");
        }
    }
}

