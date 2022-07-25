package encode;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class EncodeMFDFStringV1Test {
    String msg;
    int format8;
    int format16;
    int formatInvalid;

    @BeforeEach
    private void setData(){
        msg = "This is a test message. Here lays some text to test the format thing.";
        format8 = 8;
        format16 = 16;
        formatInvalid = 32;
    }
}