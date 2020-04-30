import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.Byte.parseByte;
import static java.lang.System.exit;

public class Main {
    /**
     * Calculate word length for given integer (used to expand table)
     * Ex: 1025 - 10
     *     1023 - 9
     * @param integer
     * @return
     */
    public static Integer calcWordLength(Integer integer) {
        int pw = 1;
        // 32 - max word length
        for (int i = 0; i < 32; i++) {
            if (pw >= integer) {
                //System.out.println("calcWordLength: " + integer + " i: " + i);
                return i;
            }
            pw *= 2;
        }
        return 0;
    }

    /**
     * Return binary string with given length
     * @param wordLength - length of output string
     * @param integer - integer to convert to binary
     * @return string
     */
    public static String getBinaryWithGivenWordLength(Integer wordLength, Integer integer) {
        String value = "";
        int pw = 1;
        for (int i = 0; i < wordLength; i++) {
            pw *= 2;
        }

        pw /= 2;

        for (int i = 0; i < wordLength; i++) {
            if (integer >= pw) {
                value += "1";
                integer -= pw;
            } else {
                value += "0";
            }
            pw /= 2;
        }

        return value;
    }


//    /**
//     * Return Integer from binary string
//     * @param s - binary string
//     * @return parsed Integer
//     */
//    public static Integer getIntegerFromBinary(String s) {
//        Integer value = 0;
//        int pw = 1;
//
//        for (int i = 0; i < s.length(); i++) {
//            if (s.charAt(s.length() - i - 1) == '1')
//                value += pw;
//
//            pw *= 2;
//        }
//
//        return value;
//    }

    /**
     * Return SIGNED Byte from binary string
     * @param s - binary string
     * @return parsed Byte
     */
    public static Byte getByteFromBinary(String s) {
        byte value = 0;
        byte pw = 1;

        for (int i = 0; i < s.length() - 1; i++) {
            if (s.charAt(s.length() - i - 1) == '1') {
                    value += pw;

            }

            pw *= 2;
        }

        if (s.charAt(0) == '1')
            value *= -1;

        if (s.charAt(0) == '1' && value == 0)
            value = -128;

        return value;
    }

    public static String getBinaryFromByte(byte b) {
        String value = "";
        int pw = 1;
        for (int i = 0; i < 6; i++) {
            pw *= 2;
        }

        if (b < 0) {
            value += "1";
            b *= -1;
        } else
            value += "0";

        for (int i = 0; i < 7; i++) {
            if (b >= pw) {
                value += "1";
                b -= pw;
            } else {
                value += "0";
            }
            pw /= 2;
        }

        return value;
    }

    static ArrayList<Byte> byteArrayList = new ArrayList<>();
    public static OutputStream outputLZWStream;
    static private String __binary = "";

    public static void outputToArchive(String _binary) {
        try {
            String binary = __binary + _binary;
            while (binary.length() >= 8) {
                outputLZWStream.write(getByteFromBinary(binary.substring(0, 8)));
                binary = binary.substring(8);
            }
            __binary = binary;
        } catch (IOException ex) {
        }
    }

    public static OutputStream outputStream;
    static private ArrayList<Byte> inputArchive;
    static private Integer inputArchiveIndex = 0;
    static private String binaryBuffer = "";

    //KEKW
    public static void outputToOutput(ArrayList<Byte> bytes) {
        try {
            for (Byte b : bytes) {
                outputStream.write(b);
            }
        } catch (IOException ex) {
        }
    }

    public static void encode(ArrayList<Byte> inputBytes) {
        int tableSize =  257;
        int wordLength = 9; //Word length in bits (9 because we have 8bit alphabet)
        ArrayList<Byte> outputStream = new ArrayList<>();

        TreeMap<ArrayList<Byte>, BitArray> table = new TreeMap<ArrayList<Byte>, BitArray>(new Comparator<ArrayList<Byte>>() {
            @Override
            public int compare(ArrayList<Byte> o1, ArrayList<Byte> o2) {
                for (int i=0; i<Math.min(o1.size(), o2.size()); i++) {
                    if (o1.get(i) < o2.get(i))
                        return -1;
                    if (o1.get(i) > o2.get(i))
                        return 1;
                }
                if (o1.size() < o2.size())
                    return -1;
                if (o1.size() > o2.size())
                    return 1;
                return 0;
            }
        });

        Integer k = 0;
        Byte _b = -128;
        while (true) {
            ArrayList<Byte> arr = new ArrayList<>();
            arr.add(_b);
            table.put(arr, new BitArray(8, k++));
            _b++;
            if (_b == 127) {
                arr = new ArrayList<>();
                arr.add(_b);
                table.put(arr, new BitArray(8, k++));
                break;
            }
        }
//        for (Byte i = -128; i <= 127 ; i++) {
//
//            arr.add(i);
//            //Add alphabet (of bytes) in table
//            //System.out.println(i);
//            table.put(arr, new BitArray(8, k++));
//        }
//        ArrayList<Byte> arr = new ArrayList<>();
//        Byte bbbb = 127;
//        arr.add(bbbb);
//        //Add alphabet (of bytes) in table
//        System.out.println("bbbb: " + bbbb);
//        table.put(arr, new BitArray(8, k++));

        /**
         * max word length of outputted token (only increase)
         * if we get BitArray from string with WordLength > maxWordLength => maxWordLength := WordLength
         */
        //Integer maxWordLength = 8;

        ArrayList<Byte> currentBytes = new ArrayList<>();
        currentBytes.add(inputBytes.get(0));
        for (int i = 1; i < inputBytes.size(); i++) {
            Byte b = inputBytes.get(i);

            //TODO: fix (do not create copy of array each time)
            ArrayList<Byte> tempCurrentBytes = new ArrayList<>(currentBytes);
            tempCurrentBytes.add(b);
            //TODO ended

            if (table.containsKey(tempCurrentBytes)) {
                currentBytes.add(b);
            } else {
                //System.out.print(getBinaryWithGivenWordLength(wordLength, table.get(currentBytes)) + " ");
                /**
                 * Get value from key-value string and convert in to binary according to maxWordLength
                 */
                BitArray bitArray = table.get(currentBytes);
                //maxWordLength = Math.max(wordLength, bitArray.length);

                    //System.out.println("Kappa" + table.get(currentBytes) + " curBytes: " + currentBytes + " size: " + tableSize + " " + b);
                String binaryString = getBinaryWithGivenWordLength(wordLength, bitArray.getIntegerValue());
                outputToArchive(binaryString); //TODO remove (only for testing)

                /*while (binary.length() >= 8) {
                    String oneByte = binary.substring(0, 8);
                    System.out.println(oneByte);
                    binary = binary.substring(8);
                    byteArrayList.add(getByteFromBinary(oneByte));
                }*/

                ArrayList<Byte> arrayToPut = new ArrayList<>(currentBytes);
                arrayToPut.add(b);

                //if (tableSize < 100000) {
                    table.put(arrayToPut, new BitArray(wordLength, tableSize));
                    tableSize++;
                    wordLength = calcWordLength(tableSize);
                //}
                //System.out.println(tableSize);
                /**
                 * recalculate word length 8 => 9 => 10 => ... bits
                 */

                currentBytes.clear();
                currentBytes.add(b);
            }
        }
        //TODO fix (do not print last byte without it)
        //currentBytes.clear();
        //currentBytes.add(inputBytes.get(inputBytes.size() - 1));
        //TODO ended
        //System.out.println(getBinaryWithGivenWordLength(tableSize, table.get(currentBytes)));
        /**
         * Get value from key-value string and convert in to binary according to maxWordLength
         */
        BitArray bitArray = table.get(currentBytes);
        //maxWordLength = Math.max(maxWordLength, bitArray.length);
        String binaryString = getBinaryWithGivenWordLength(wordLength, bitArray.getIntegerValue());
        outputToArchive(binaryString);
        /*while (binary.length() >= 8) {
            String oneByte = binary.substring(0, 8);
            System.out.println(oneByte);
            binary = binary.substring(8);
            byteArrayList.add(getByteFromBinary(oneByte));
        }
        if (binary.length() != 0) {
            while (binary.length() != 8)
                binary += "0";
            String oneByte = binary.substring(0, 8);
            System.out.println(oneByte);
            binary = binary.substring(8);
            byteArrayList.add(getByteFromBinary(oneByte));
        }*/
    }

    /**
     * Returns next token from bytes stream
     * @param wordLength - read next wordLength bits (not bytes!!!)
     * @return Integer
     */
    public static Integer getNextToken(Integer wordLength) {
        /**
         * get next wordLength chars and delete them from the beginning
         */
        while (binaryBuffer.length() < wordLength * 10) {
            if (inputArchiveIndex >= inputArchive.size())
                break;
            binaryBuffer += getBinaryFromByte(inputArchive.get(inputArchiveIndex));
            inputArchiveIndex++;
        }

        if (binaryBuffer.length() == 0 || binaryBuffer.length() < wordLength)
            return -1;

//        while (binary.length() < wordLength)
//            binary = "0" + binary;

        String nextString = binaryBuffer.substring(0, wordLength);
        binaryBuffer = binaryBuffer.substring(wordLength);

        System.out.println("next token: " + nextString + " " + Integer.parseInt(nextString, 2) + " length: " + wordLength);
        return Integer.parseInt(nextString, 2);
    }

    public static void decode() {
        int tableSize =  257;
        int wordLength = 9; //Word length in bits

        TreeMap<Integer, ArrayList<Byte>> table = new TreeMap<Integer, ArrayList<Byte>>();

        Integer k = 0;
        Byte _b = -128;
        while (true) {
            ArrayList<Byte> arr = new ArrayList<>();
            arr.add(_b);
            table.put(k++, arr);
            _b++;
            if (_b == 127) {
                arr = new ArrayList<>();
                arr.add(_b);
                table.put(k++, arr);
                break;
            }
        }

        /**
         * max word length of outputted token (only increase)
         * if we get BitArray from string with WordLength > maxWordLength => maxWordLength := WordLength
         */
        //Integer maxWordLength = 8;

        //String byteString = "";
        //ArrayList<Integer> bytes = new ArrayList<>(); //Contains codes not bytes
        /*for (char c : binary.toCharArray()) {
            if (c != ' ') {
                byteString += c;
            } else {
                Integer b = Integer.parseInt(byteString, 2);
                byteString = "";

                bytes.add(b);
            }
        }*/

        boolean isZeroIndex = true;
        //System.out.print(table.get(bytes.get(0)).get(0) + " ");
        ArrayList<Byte> currentBytes = new ArrayList<>();
        //Integer oldToken = bytes.get(0);
        Integer oldToken = getNextToken(wordLength);
        outputToOutput(table.get(oldToken));
        for (Byte b : table.get(oldToken))
            currentBytes.add(b);
        Byte C = table.get(oldToken).get(0); //TODO rename
        while (true) {
            //Skip first symbol
            if (isZeroIndex) {
                isZeroIndex = false;
                continue;
            }

            //BitArray currentToken = new BitArray(wordLength, currentByte);

            Integer currentToken = getNextToken(wordLength);
            if (currentToken == -1)
                break;

            if (!table.containsKey(currentToken)) {
                currentBytes = new ArrayList<>();
                for (Byte b : table.get(oldToken))
                    currentBytes.add(b);
                currentBytes.add(C);
            } else {
                currentBytes = new ArrayList<>();
                for (Byte b : table.get(currentToken))
                    currentBytes.add(b);
            }

            outputToOutput(currentBytes);
//            for (Byte b : currentBytes) {
//                outputStream.add(b);
//            }
            //System.out.print(currentBytes.toString() + " ");
            C = currentBytes.get(0);
            ArrayList<Byte> arrayToPut = new ArrayList<>();
            for (Byte b : table.get(oldToken))
                arrayToPut.add(b);
            arrayToPut.add(C);

            //System.out.println(oldToken + " " + currentToken + " " + tableSize);
            table.put(tableSize, arrayToPut);
            tableSize++;
//            if (tableSize == 514) {
//                for (int j = 0; j < decoding.size(); j++) {
//                    System.out.println(encoding.get(j) + " " + decoding.get(j));
//                }
//                exit(0);
//            }
            wordLength = calcWordLength(tableSize + 1);

            /**
             * recalculate word length 8 => 9 => 10 => ... bits
             */

            //System.out.println(tableSize + " " + wordLength);

            oldToken = currentToken;
        }
    }

    public static void main(String[] args) throws IOException {

//        System.out.println(getBinaryWithGivenWordLength(8, 10));
//        Byte bbb = 3;
//        System.out.println(getBinaryFromByte(bbb));
        //System.out.println((byte)(int)Integer.valueOf("01111111", 2));
        //System.out.println(getByteFromBinary("10000000"));

        String filename = "src/264539.jpg";

        byte[] fileContents =  Files.readAllBytes(Paths.get(filename));
        ArrayList<Byte> input = new ArrayList<Byte>();
        //System.out.println("input in bytes: ");
        for (byte b : fileContents) {
            //System.out.print(b + " ");
            input.add(b);
        }


        outputLZWStream = new BufferedOutputStream(new FileOutputStream("./src/archive.lzw"));
        encode(input);
        if (__binary.length() > 0) {
            while (__binary.length() != 8) {
                __binary = "0" + __binary;
            }
            outputLZWStream.write(getByteFromBinary(__binary));
        }
        outputLZWStream.close();
        //System.out.println("Binary after encoding: ");
        //System.out.println(binary);


        //ArrayList<Byte> encoding = new ArrayList<>();
        //while (binary.length() % 8 != 0)
        //    binary += "0";
//        for (int i = 0; i < binary.length(); i += 8) {
//            if (i + 8 >= binary.length()) {
//                String currentByte = binary.substring(i);
//                while (currentByte.length() < 8)
//                    currentByte = currentByte + "0";
//                //System.out.println("currentByte  " + currentByte);
//                Byte bb = getByteFromBinary(currentByte);
//                //System.out.println(bb);
//                encoding.add(bb);
//                break;
//            }
//            String currentByte = binary.substring(i, i + 8);
//            System.out.print(currentByte + " ");
//            Byte bb = getByteFromBinary(currentByte);
//            //System.out.println(bb);
//            encoding.add(bb);
//        }
//        for (Byte b : encoding) {
//            outputLZWStream.write(b);
//        }
//        outputLZWStream.close();
//        System.out.println(input.size());
//
//        binary = "";

        byte[] bytes = Files.readAllBytes(Paths.get("./src/archive.lzw"));
        inputArchive =  new ArrayList<Byte>();
        for (byte i : bytes) inputArchive.add(i);
//        for (byte b : inputArchive) {
//            //System.out.println(b + " " + getBinaryFromByte(b));
//            binary += getBinaryFromByte(b);
//            //System.out.println(getBinaryFromByte(b) + " " + b);
//        }

        //System.out.println("Binary after reading: ");
        //System.out.println(binary);

        //System.out.println("Encoded in bytes: ");
        //ArrayList<Byte> output = decode();
        //coddingClass.makeDict("abacabadabacabae");
        outputStream = new BufferedOutputStream(new FileOutputStream("./src/output.txt"));
        decode();
//        for (Byte b : output) {
//            outputStream.write(b);
//        }
        outputStream.close();
        //LZVarhiver.MakeDictionaryBigger("abacabadabacabae");
    }
}