import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class MyToken {

    MyReader reader;
    ArrayList<Byte> buffer = new ArrayList<Byte>();
    private Integer inputArchiveIndex = 0;
    private String binaryBuffer = "";

    MyToken(MyReader reader) {
        this.reader = reader;
    }

    /**
     * Returns next token from bytes stream
     * @param wordLength - read next wordLength bits (not bytes!!!)
     * @return Integer
     */
    public Integer getNextToken(Integer wordLength) {
        /**
         * get next wordLength chars and delete them from the beginning
         */
        if (binaryBuffer.length() < 10000) {
            try {
                //System.out.println("GET HERE");
                buffer = reader.readBytes(10000);
            } catch (Exception ex) {
                //System.out.println("GET HERE");
            }
        }
        while (buffer.size() > 0) {
            binaryBuffer += Utils.getBinaryFromByte(buffer.get(0));
            buffer.remove(0);
        }

        if (binaryBuffer.length() == 0 || binaryBuffer.length() < wordLength)
            return -1;

//        while (binary.length() < wordLength)
//            binary = "0" + binary;
        //System.out.println("GET HERE");

        String nextString = binaryBuffer.substring(0, wordLength);
        binaryBuffer = binaryBuffer.substring(wordLength);

        //System.out.println("next token: " + nextString + " " + Integer.parseInt(nextString, 2) + " length: " + wordLength);

        return Integer.parseInt(nextString, 2);
    }

    public boolean hasNextByte() {
        if (buffer.size() == 0) {
            try {
                buffer = reader.readBytes(10000);
            } catch (IOException ex) {

            }
        }
        if (buffer.size() > 0)
            return true;
        else
            return false;
    }

    public Byte getNextByte() throws Exception {
        if (hasNextByte()) {
            Byte first = buffer.get(0);
            buffer.remove(0);
            return first;
        } else {
            throw new Exception("Doesnt have next byte");
        }
    }

}
