import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class MyToken {

    MyReader reader;
    ArrayList<Byte> buffer = new ArrayList<Byte>();
    private Integer i1 = 0;
    private String binaryBuffer = "";
    private Integer bufferIndex = 0;

    MyToken(MyReader reader) {
        this.reader = reader;
    }

    /**
     * Returns next token from bytes stream
     * @param wordLength - read next wordLength bits (not bytes!!!)
     * @return Integer
     */
    public Integer getNextToken(Integer wordLength) {


        String binary = binaryBuffer;
        binaryBuffer = "";
        while (true) {
            //System.out.println("pre-if: " + binary + " " + binaryBuffer + " " + wordLength);
            if (bufferIndex >= buffer.size()) {
                try {
                    buffer = reader.readBytes(10000);
                    bufferIndex = 0;
                } catch (Exception ex) {

                }
            }
            if (buffer.size() == 0)
                return -1;
            if (binary.length() + 8 > wordLength) {
                String _binary = Utils.getBinaryFromByte(buffer.get(bufferIndex++));
                Integer idx = wordLength - binary.length();
                //System.out.println("if: " + binary + " " + _binary);
                binary += _binary.substring(0, idx);
                binaryBuffer += _binary.substring(idx);
                break;
            } else {
                //binaryBuffer = "";
                binary += Utils.getBinaryFromByte(buffer.get(bufferIndex++));
            }
        }

        //System.out.println(binary + " " + wordLength + " " + binaryBuffer);
        return Integer.parseInt(binary, 2);
    }

    public boolean hasNextByte() {
        try {
            if (bufferIndex >= buffer.size()) {
                buffer = reader.readBytes(10000);
                bufferIndex = 0;
            }
        } catch (Exception ex) {

        }
//        if (buffer.size() == 0) {
//            try {
//                //Integer _sz = buffer.size();
//                //System.out.println("prev: " + buffer.toString());
//                buffer = reader.readBytes(100000);
//                //bufferIndex = 0;
////                System.out.println(buffer);
////                if (_sz != buffer.size())
////                    bufferIndex = 0;
//                //System.out.println("after: " + buffer.toString());
//            } catch (IOException ex) {
//
//            }
//        }
        if (buffer.size() > 0)
            return true;
        else
            return false;
    }

    public Byte getNextByte() throws Exception {
        if (hasNextByte()) {
            Byte first = buffer.get(bufferIndex);
            bufferIndex++;
            //buffer.remove(0);
            return first;
//            Byte first = buffer.get(0);
//            buffer.remove(0);
//            return first;
        } else {
            throw new Exception("Doesnt have next byte");
        }
    }

}
