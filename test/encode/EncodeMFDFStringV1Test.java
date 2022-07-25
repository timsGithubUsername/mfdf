package encode;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EncodeMFDFStringV1Test {
    static String msg;
    static String msg16;
    static String name;
    static int format8;
    static int format16;
    static int formatInvalid;
    static EncodeMFDF encoder;

    @BeforeAll
    private static void setData(){
        msg = "This is a test message. Here lays some text to test the format thing.";
        msg16 = "here are some utf16 symbols: ϾϾϾϴϪϜ";
        name = "MFDF";
        format8 = 8;
        format16 = 16;
        formatInvalid = 32;

        encoder = new EncodeMFDFStringV1();
    }

    @Test
    //only Message
    void testGetByteCode1() {
        //normal message
        byte[] mfdfBytes = encoder.getByteCode(msg);

        int size = msg.length();
        int format = 16;

        byte[] nameBytes = Arrays.copyOfRange(mfdfBytes, 0, 4);
        byte[] formatBytes = Arrays.copyOfRange(mfdfBytes, 4, 8);
        byte[] sizeBytes = Arrays.copyOfRange(mfdfBytes, 8, 12);
        byte[] msgBytes = Arrays.copyOfRange(mfdfBytes, 12, mfdfBytes.length);

        assertEquals(name, new String(nameBytes, StandardCharsets.UTF_8));
        assertEquals(format, fromByteArray(formatBytes));
        assertEquals(size, fromByteArray(sizeBytes));
        assertEquals(msg, new String(msgBytes, StandardCharsets.UTF_16));

        //with utf 16 symbols
        mfdfBytes = encoder.getByteCode(msg16, format16);

        size = msg16.length();

        nameBytes = Arrays.copyOfRange(mfdfBytes, 0, 4);
        formatBytes = Arrays.copyOfRange(mfdfBytes, 4, 8);
        sizeBytes = Arrays.copyOfRange(mfdfBytes, 8, 12);
        msgBytes = Arrays.copyOfRange(mfdfBytes, 12, mfdfBytes.length);

        assertEquals(name, new String(nameBytes, StandardCharsets.UTF_8));
        assertEquals(format, fromByteArray(formatBytes));
        assertEquals(size, fromByteArray(sizeBytes));
        assertEquals(msg16, new String(msgBytes, StandardCharsets.UTF_16));
    }

    @Test
    void testGetByteCode2() {
        //utf8
        byte[] mfdfBytes = encoder.getByteCode(msg, format8);

        int size = msg.length();
        int format = 8;

        byte[] nameBytes = Arrays.copyOfRange(mfdfBytes, 0, 4);
        byte[] formatBytes = Arrays.copyOfRange(mfdfBytes, 4, 8);
        byte[] sizeBytes = Arrays.copyOfRange(mfdfBytes, 8, 12);
        byte[] msgBytes = Arrays.copyOfRange(mfdfBytes, 12, mfdfBytes.length);

        assertEquals(name, new String(nameBytes, StandardCharsets.UTF_8));
        assertEquals(format, fromByteArray(formatBytes));
        assertEquals(size, fromByteArray(sizeBytes));
        assertEquals(msg, new String(msgBytes, StandardCharsets.UTF_8));

        //utf16
        mfdfBytes = encoder.getByteCode(msg, format16);

        size = msg.length();
        format = 16;

        nameBytes = Arrays.copyOfRange(mfdfBytes, 0, 4);
        formatBytes = Arrays.copyOfRange(mfdfBytes, 4, 8);
        sizeBytes = Arrays.copyOfRange(mfdfBytes, 8, 12);
        msgBytes = Arrays.copyOfRange(mfdfBytes, 12, mfdfBytes.length);

        assertEquals(name, new String(nameBytes, StandardCharsets.UTF_8));
        assertEquals(format, fromByteArray(formatBytes));
        assertEquals(size, fromByteArray(sizeBytes));
        assertEquals(msg, new String(msgBytes, StandardCharsets.UTF_16));

        //invalid
        mfdfBytes = encoder.getByteCode(msg, formatInvalid);

        size = msg.length();

        nameBytes = Arrays.copyOfRange(mfdfBytes, 0, 4);
        formatBytes = Arrays.copyOfRange(mfdfBytes, 4, 8);
        sizeBytes = Arrays.copyOfRange(mfdfBytes, 8, 12);
        msgBytes = Arrays.copyOfRange(mfdfBytes, 12, mfdfBytes.length);

        assertEquals(name, new String(nameBytes, StandardCharsets.UTF_8));
        assertEquals(format, fromByteArray(formatBytes));
        assertEquals(size, fromByteArray(sizeBytes));
        assertEquals(msg, new String(msgBytes, StandardCharsets.UTF_16));
    }

    private int fromByteArray(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24) |
                ((bytes[1] & 0xFF) << 16) |
                ((bytes[2] & 0xFF) << 8 ) |
                ((bytes[3] & 0xFF));
    }

    private void printByteArray(byte[] array, int format){
        for (byte b : array) {
            System.out.print(b + " ");
        }
        System.out.println("\n");
    }
}