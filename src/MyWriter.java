import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyWriter {

    private OutputStream stream;
    static private String __binary = "";

    MyWriter(String filename) {
        try {
            stream = new BufferedOutputStream(new FileOutputStream(filename));
        } catch (IOException ex) {

        }
    }

    public void write(String _binary) {
        try {
            String binary = __binary + _binary;
            //System.out.println(_binary);
            while (binary.length() >= 8) {
                stream.write(Utils.getByteFromBinary(binary.substring(0, 8)));
                binary = binary.substring(8);
            }
            __binary = binary;
        } catch (IOException ex) {
        }
    }

    public void write(ArrayList<Byte> bytes) {
        try {
            for (Byte b : bytes) {
                stream.write(b);
            }
        } catch (IOException ex) {
        }
    }

    public void endWriteStream() {
        try {
            if (__binary.length() > 0) {
                while (__binary.length() != 8) {
                    __binary = __binary + "0";
                }
                stream.write(Utils.getByteFromBinary(__binary));
                __binary = "";
            }
            stream.close();
        } catch (IOException ex) {

        }
    }

}
