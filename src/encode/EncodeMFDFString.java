package encode;

public interface EncodeMFDFString {
    byte[] getByteCode(String msg);
    byte[] getByteCode(String msg, int format);
}
