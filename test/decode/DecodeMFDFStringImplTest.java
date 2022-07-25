package decode;

import encode.EncodeMFDFString;
import encode.EncodeMFDFStringImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DecodeMFDFStringImplTest {
    static String msg;
    static String msg16;
    static String name;
    static int format8;
    static int format16;
    static int formatInvalid;
    static EncodeMFDFString encoder;
    static DecodeMFDFString decoder;

    @BeforeAll
    private static void setData(){
        msg = "This is a test message. Here lays some text to test the format thing.";
        msg16 = "here are some utf16 symbols: ϾϾϾϴϪϜ";
        name = "MFDF";
        format8 = 8;
        format16 = 16;
        formatInvalid = 32;

        encoder = new EncodeMFDFStringImpl();
        decoder= new DecodeMFDFStringImpl();
    }

    @Test
    void getFormatFromHeader() {
        byte[] header = Arrays.copyOfRange(encoder.getByteCode(msg), 0, 12);
        assertEquals(format16, decoder.getFormatFromHeader(header));

        header = Arrays.copyOfRange(encoder.getByteCode(msg16), 0, 12);
        assertEquals(format16, decoder.getFormatFromHeader(header));

        header = Arrays.copyOfRange(encoder.getByteCode(msg, format16), 0, 12);
        assertEquals(format16, decoder.getFormatFromHeader(header));

        header = Arrays.copyOfRange(encoder.getByteCode(msg16, format8), 0, 12);
        assertEquals(format8, decoder.getFormatFromHeader(header));

        header = Arrays.copyOfRange(encoder.getByteCode(msg16, formatInvalid), 0, 12);
        assertEquals(format16, decoder.getFormatFromHeader(header));
    }

    @Test
    void getSizeFromHeader() {
        int msgSize = msg.length();
        int msg16Size = msg16.length();

        byte[] header = Arrays.copyOfRange(encoder.getByteCode(msg), 0, 12);
        assertEquals(msgSize, decoder.getSizeFromHeader(header));

        header = Arrays.copyOfRange(encoder.getByteCode(msg16), 0, 12);
        assertEquals(msg16Size, decoder.getSizeFromHeader(header));

        header = Arrays.copyOfRange(encoder.getByteCode(msg, format16), 0, 12);
        assertEquals(msgSize, decoder.getSizeFromHeader(header));

        header = Arrays.copyOfRange(encoder.getByteCode(msg, format8), 0, 12);
        assertEquals(msgSize, decoder.getSizeFromHeader(header));

        header = Arrays.copyOfRange(encoder.getByteCode(msg, formatInvalid), 0, 12);
        assertEquals(msgSize, decoder.getSizeFromHeader(header));
    }

    @Test
    void testGetMessageFromData1() {
        byte[] data = Arrays.copyOfRange(encoder.getByteCode(msg), 12, encoder.getByteCode(msg).length);
        assertEquals(msg, decoder.getMessageFromData(data));

        data = Arrays.copyOfRange(encoder.getByteCode(msg16), 12, encoder.getByteCode(msg16, format16).length);
        assertEquals(msg16, decoder.getMessageFromData(data));

        data = Arrays.copyOfRange(encoder.getByteCode(msg, format16), 12, encoder.getByteCode(msg, format16).length);
        assertEquals(msg, decoder.getMessageFromData(data));


        data = Arrays.copyOfRange(encoder.getByteCode(msg, format8), 12, encoder.getByteCode(msg, format8).length);
        assertEquals(new String (msg.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_16), decoder.getMessageFromData(data));

        data = Arrays.copyOfRange(encoder.getByteCode(msg, formatInvalid), 12, encoder.getByteCode(msg, formatInvalid).length);
        assertEquals(msg, decoder.getMessageFromData(data));
    }

    @Test
    void testGetMessageFromData2() {
        byte[] data = Arrays.copyOfRange(encoder.getByteCode(msg), 12, encoder.getByteCode(msg).length);
        assertEquals(msg, decoder.getMessageFromData(data, format16));

        data = Arrays.copyOfRange(encoder.getByteCode(msg16), 12, encoder.getByteCode(msg16).length);
        assertEquals(msg16, decoder.getMessageFromData(data, format16));

        data = Arrays.copyOfRange(encoder.getByteCode(msg, format16), 12, encoder.getByteCode(msg, format16).length);
        assertEquals(msg, decoder.getMessageFromData(data, format16));

        data = Arrays.copyOfRange(encoder.getByteCode(msg, format8), 12, encoder.getByteCode(msg, format8).length);
        assertEquals(msg, decoder.getMessageFromData(data, format8));

        data = Arrays.copyOfRange(encoder.getByteCode(msg, formatInvalid), 12, encoder.getByteCode(msg, formatInvalid).length);
        assertEquals(msg, decoder.getMessageFromData(data, format16));
    }

    @Test
    void getMessage() {
        byte[] mfdfDefault = encoder.getByteCode(msg);
        byte[] mfdfDefaultMessage16 = encoder.getByteCode(msg16);
        byte[] mfdfUTF8 = encoder.getByteCode(msg, format8);

        assertEquals(msg, decoder.getMessage(mfdfDefault));
        assertEquals(msg16, decoder.getMessage(mfdfDefaultMessage16));
        assertEquals(msg, decoder.getMessage(mfdfUTF8));
    }
}