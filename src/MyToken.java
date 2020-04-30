import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class MyToken {

    ArrayList<Byte> bytes;

    MyToken(ArrayList<Byte> bytes) {
        this.bytes = bytes;
    }

    private Integer inputArchiveIndex = 0;
    private String binaryBuffer = "";

    /**
     * Returns next token from bytes stream
     * @param wordLength - read next wordLength bits (not bytes!!!)
     * @return Integer
     */
    public Integer getNextToken(Integer wordLength) {
        /**
         * get next wordLength chars and delete them from the beginning
         */
        while (binaryBuffer.length() < wordLength * 10) {
            if (inputArchiveIndex >= bytes.size())
                break;
            binaryBuffer += Utils.getBinaryFromByte(bytes.get(inputArchiveIndex));
            inputArchiveIndex++;
        }

        if (binaryBuffer.length() == 0 || binaryBuffer.length() < wordLength)
            return -1;

//        while (binary.length() < wordLength)
//            binary = "0" + binary;

        String nextString = binaryBuffer.substring(0, wordLength);
        binaryBuffer = binaryBuffer.substring(wordLength);

        //System.out.println("next token: " + nextString + " " + Integer.parseInt(nextString, 2) + " length: " + wordLength);
        return Integer.parseInt(nextString, 2);
    }

}
