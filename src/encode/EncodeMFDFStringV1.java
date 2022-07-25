package encode;

import java.nio.charset.StandardCharsets;

public class EncodeMFDFStringV1 implements EncodeMFDF {
    final byte[] NAME = "MFDF".getBytes(StandardCharsets.UTF_8);
    final int FORMAT = 16;

    /**
     * Get the byte code in MFDF-Format with an UTF16 Message
     * @param msg the message contained in the byte code
     * @return a byte array in MFDF-Format
     */
    @Override
    public byte[] getByteCode(String msg) {
        return getByteCode(msg, FORMAT);
    }

    /**
     * Get the byte code in MFDF-Format with an UTF8 or UTF16 Message
     * @param msg the message contained in the byte code
     * @param format an int to declare the UTF-Format. 8 for UTF8, 16 for UTF16. Every other Input leads to an encoding
     *               in UTF16.
     * @return a byte array in MFDF-Format
     */
    @Override
    public byte[] getByteCode(String msg, int format) {
        int msgLength = msg.length();

        byte[] msgBytes;
        byte[] headerBytes;

        try {
            msgBytes = switch (format) {
                case 8 -> msg.getBytes(StandardCharsets.UTF_8);
                case 16 -> msg.getBytes(StandardCharsets.UTF_16);
                default -> throw new Exception("Wrong format. Using UTF16 for this Message!");
            };
            headerBytes = prepareHeader(toByteArray(msgLength), toByteArray(format));

        } catch (Exception e) {
            e.printStackTrace();
            msgBytes = msg.getBytes(StandardCharsets.UTF_16);
            headerBytes = prepareHeader(toByteArray(msgLength), toByteArray(FORMAT));
        }

        byte[] output = new byte[msgBytes.length + headerBytes.length];

        fillArray(output, headerBytes, 0);
        fillArray(output, msgBytes, headerBytes.length);

        return output;
    }

    //prepares MFDF-Header
    //Name: MFDF; 4 Byte
    //Format: 8, 16, 32; 4 Byte
    //Size: size of the Data Chunk; 4 Byte
    private byte[] prepareHeader(byte[] size, byte[] format) {
        byte[] output = new byte[16];
        fillArray(output, NAME, 0);
        fillArray(output, format, 4);
        fillArray(output, size, 8);
        return null;
    }

    //convert a int to a 4 long Byte array
    private byte[] toByteArray(int value) {
        return new byte[]{
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value};
    }

    //fill a array with values from another array
    private void fillArray(byte[] arrayToFill, byte[] content, int offset) {
        try {
            if (content.length + offset > arrayToFill.length) throw new Exception("content array to long!");
            for (int i = offset; i - offset < content.length; i++) arrayToFill[i] = content[i - offset];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
