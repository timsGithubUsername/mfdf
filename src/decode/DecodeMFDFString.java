package decode;

public interface DecodeMFDFString {
    int getFormatFromHeader(byte[] header);
    int getSizeFromHeader(byte[] header);
    String getMessageFromData(byte[] message);
    String getMessageFromData(byte[] message, int format);
    String getMessage(byte[] mfdfBytes);
}
