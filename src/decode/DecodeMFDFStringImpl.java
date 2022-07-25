package decode;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class DecodeMFDFStringImpl implements DecodeMFDFString {
    /*
     NAME | FORMAT | SIZE | DATA
    |--------Header-------|-data-|

    Header:
    Name (4Bytes): "MFDF"
    Format (4Bytes): 16 or 8 for UTF16 or UTF8
    Size (4Bytes): Number of Symbols in the Message

    Data:
    Bytes for the String in UTF16 or UTF8
     */
    final int FORMAT_OFFSET = 4, SIZE_OFFSET = 8, HEADER_SIZE = 12;

    @Override
    public int getFormatFromHeader(byte[] header) {
        return intFromByteArray(Arrays.copyOfRange(header, FORMAT_OFFSET, SIZE_OFFSET));
    }

    @Override
    public int getSizeFromHeader(byte[] header) {
        return intFromByteArray(Arrays.copyOfRange(header, SIZE_OFFSET, HEADER_SIZE));
    }

    @Override
    public String getMessageFromData(byte[] message) {
        return new String(message, StandardCharsets.UTF_16);
    }

    @Override
    public String getMessageFromData(byte[] message, int format) {
        String output;
        try {
            output = switch (format) {
                case 8 -> new String(message, StandardCharsets.UTF_8);
                case 16 -> new String(message, StandardCharsets.UTF_16);
                default -> throw new Exception("Wrong format. Using UTF16 for this Message!");
            };
        } catch (Exception e) {
            e.printStackTrace();
            output = new String(message, StandardCharsets.UTF_16);
        }
        return output;
    }

    @Override
    public String getMessage(byte[] mfdfBytes) {
        byte[] header = Arrays.copyOfRange(mfdfBytes, 0, HEADER_SIZE);
        return getMessageFromData(Arrays.copyOfRange(mfdfBytes, HEADER_SIZE, mfdfBytes.length), getFormatFromHeader(header));
    }

    private int intFromByteArray(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24) |
                ((bytes[1] & 0xFF) << 16) |
                ((bytes[2] & 0xFF) << 8) |
                ((bytes[3] & 0xFF));
    }
}
