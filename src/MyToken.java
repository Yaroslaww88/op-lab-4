import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class MyToken {

    MyReader reader;
    ArrayList<Byte> buffer = new ArrayList<Byte>();
    private Integer i1 = 0;
    private String binaryBuffer = "";
    private Integer bufferIndex = 0;
    public Long len = 0L;
    public String __binary = "";

    MyToken(MyReader reader) {
        this.reader = reader;

        buffer.clear();

//        for (Byte b : buffer) {
//            __binary += Utils.getBinaryFromByte(b);
//        }

    }

    private boolean f = false;

    /**
     * Returns next token from bytes stream
     * @param wordLength - read next wordLength bits (not bytes!!!)
     * @return Integer
     */
    public Integer getNextToken(Integer wordLength) {

        try {
            if (buffer.size() == 0)
            //buffer.clear();
                buffer = reader.readBytes();
                //System.out.println("sz: " + buffer.size());
        } catch (Exception ex) {

        }

//        if (bufferIndex >= buffer.size()) {
//            try {
//                buffer.clear();
//                buffer.addAll(reader.readBytes(1000000));
//               // System.out.println("sz: " + buffer.size());
//                bufferIndex = 0;
//                len += buffer.size();
//            } catch (Exception ex) {
//
//            }
//        }
//
//        String binary = binaryBuffer;
//        binaryBuffer = "";
//        System.out.println(buffer.size());
//
//        if (buffer.size() == 0 && binary.length() < wordLength)
//            return -1;
//        while (true) {
//            //System.out.println("pre-if: " + binary + " " + binaryBuffer + " " + wordLength);
////            if (bufferIndex >= buffer.size()) {
////                try {
////                    buffer.clear();
////                    buffer.addAll(reader.readBytes(10000));
////                    System.out.println("sz2: " + buffer.size());
////                    bufferIndex = 0;
////                    //len += buffer.size();
////                } catch (Exception ex) {
////
////                }
////            }
//            //System.out.println("sz3: " + buffer.size());
//            if (bufferIndex >= buffer.size())
//                break;
//
//            if (binary.length() + 8 > wordLength) {
//                String _binary = Utils.getBinaryFromByte(buffer.get(bufferIndex++));
//                Integer idx = wordLength - binary.length();
//                //System.out.println("if: " + binary + " " + _binary);
//                binary += _binary.substring(0, idx);
//                binaryBuffer += _binary.substring(idx);
//                break;
//            } else {
//                //binaryBuffer = "";
//                binary += Utils.getBinaryFromByte(buffer.get(bufferIndex++));
//            }
//        }
//
//        boolean f = false;
//        while (binary.length() < wordLength) {
////            binary = binary + "0";
////            System.out.println(len + " " + buffer.toString());
////            f = true;
//            return -1;
//            //247908
//        }

        String binary = binaryBuffer;
        binaryBuffer = "";

        while (true) {
            //System.out.println("pre-if: " + binary + " " + binaryBuffer + " " + wordLength);
//            if (bufferIndex >= buffer.size()) {
//                try {
//                    buffer = reader.readBytes(10000);
//                    bufferIndex = 0;
//                } catch (Exception ex) {
//
//                }
//            }
            if (bufferIndex >= buffer.size()) {
                if (f)
                    return -1;
                boolean ff = false;
                for (int i=0; i<binary.length(); i++) {
                    if (binary.charAt(i) != '0') {
                        ff = true;
                    }
                }
                if (!ff)
                    return -1;
                while (binary.length() < wordLength) {
                    binary = binary + "0";
                    //System.out.println(binary + " " + binary.length());
                }
                f = true;
                break;
            }
//            if (bufferIndex >= buffer.size()) {
//                try {
//                    buffer = reader.readBytes(10000000);
//                    bufferIndex = 0;
//                } catch (Exception ex) {
//
//                }
//            }
//            if (buffer.size() == 0)
//                return -1;
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
        //System.out.println(bufferIndex);
            return Integer.parseInt(binary, 2);

//        String _binary = __binary.substring(i1, i1 + wordLength);
//        //__binary = __binary.substring(wordLength);
//        i1 += wordLength;
//        if (_binary.length() < wordLength)
//            return -1;
//
//        return Integer.parseInt(_binary, 2);
    }

    public boolean hasNextByte() {
        try {
            if (bufferIndex >= buffer.size()) {
                buffer = reader.readBytes(1000000);

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
