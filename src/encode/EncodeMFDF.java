package encode;

public interface EncodeMFDF {
    byte[] getByteCode(String msg);
    byte[] getByteCode(String msg, int format);
}
