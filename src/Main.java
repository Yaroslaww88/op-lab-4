import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    static String binary = "";
    static ArrayList<Byte> byteArrayList = new ArrayList<>();

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
            if (pw >= integer)
                return i;
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


    /**
     * Return Integer from binary string
     * @param s - binary string
     * @return parsed Integer
     */
    public static Integer getIntegerFromBinary(String s) {
        Integer value = 0;
        int pw = 1;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(s.length() - i - 1) == '1')
                value += pw;

            pw *= 2;
        }

        return value;
    }

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

        return value;
    }


    public static void encode(ArrayList<Byte> inputBytes) {
        int tableSize =  256;
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
        for (Byte i = -127; i < 127 ; i++) {
            ArrayList<Byte> arr = new ArrayList<>();
            arr.add(i);
            //Add alphabet (of bytes) in table
            table.put(arr, new BitArray(8, k++));
        }

        /**
         * max word length of outputted token (only increase)
         * if we get BitArray from string with WordLength > maxWordLength => maxWordLength := WordLength
         */
        //Integer maxWordLength = 8;

        ArrayList<Byte> currentBytes = new ArrayList<>();
        currentBytes.add(inputBytes.get(0));
        boolean isZeroIndex = true;
        for (Byte b : inputBytes) {
            //Skip first symbol
            if (isZeroIndex) {
                isZeroIndex = false;
                continue;
            }

            //TODO: fix (do not create copy of array each time)
            ArrayList<Byte> tempCurrentBytes = new ArrayList<>();
            tempCurrentBytes.addAll(currentBytes);
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
                String binaryString = getBinaryWithGivenWordLength(wordLength, bitArray.getIntegerValue());
                binary += binaryString; //TODO remove (only for testing)

                /*while (binary.length() >= 8) {
                    String oneByte = binary.substring(0, 8);
                    System.out.println(oneByte);
                    binary = binary.substring(8);
                    byteArrayList.add(getByteFromBinary(oneByte));
                }*/

                currentBytes.add(b);

                //TODO: fix (do not create copy of array each time)
                ArrayList<Byte> arrayToPut = new ArrayList<>();
                for (Byte b1 : currentBytes)
                    arrayToPut.add(b1);
                //TODO ended

                table.put(arrayToPut, new BitArray(wordLength, tableSize));
                tableSize++;
                /**
                 * recalculate word length 8 => 9 => 10 => ... bits
                 */
                wordLength = calcWordLength(tableSize);

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
        binary += binaryString; //TODO remove (only for testing)
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
        if (binary.length() == 0 || binary.length() < wordLength)
            return -1;

        while (binary.length() < wordLength)
            binary = "0" + binary;

        String nextString = binary.substring(0, wordLength);
        binary = binary.substring(wordLength);

        return Integer.parseInt(nextString, 2);
    }

    public static ArrayList<Byte> decode() {
        int tableSize =  256;
        int wordLength = 9; //Word length in bits
        ArrayList<Byte> outputStream = new ArrayList<>();

        TreeMap<Integer, ArrayList<Byte>> table = new TreeMap<Integer, ArrayList<Byte>>();

        Integer k = 0;
        for (Byte i = -127; i < 127; i++) {
            ArrayList<Byte> arr = new ArrayList<>();
            arr.add(i);
            table.put(k++, arr);
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

        /**
         * init word length and table size
         */
        wordLength = 9;

        boolean isZeroIndex = true;
        //System.out.print(table.get(bytes.get(0)).get(0) + " ");
        ArrayList<Byte> currentBytes = new ArrayList<>();
        //Integer oldToken = bytes.get(0);
        Integer oldToken = getNextToken(wordLength);
        outputStream.addAll(table.get(oldToken));
        Byte C = 0; //TODO rename
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

            for (Byte b : currentBytes) {
                outputStream.add(b);
            }
            //System.out.print(currentBytes.toString() + " ");
            C = currentBytes.get(0);
            ArrayList<Byte> arrayToPut = new ArrayList<>();
            for (Byte b : table.get(oldToken))
                arrayToPut.add(b);
            arrayToPut.add(C);
            table.put(tableSize, arrayToPut);
            tableSize++;
            /**
             * recalculate word length 8 => 9 => 10 => ... bits
             */
            wordLength = calcWordLength(tableSize);

            oldToken = currentToken;
        }

        return outputStream;
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

    public static void main(String[] args) throws IOException {

        System.out.println(getBinaryWithGivenWordLength(8, 10));
        Byte bbb = 3;
        System.out.println(getBinaryFromByte(bbb));
        System.out.println(getByteFromBinary("00000011"));

        String filename = "src/input.txt";

        byte[] fileContents =  Files.readAllBytes(Paths.get(filename));
        ArrayList<Byte> input = new ArrayList<Byte>();
        System.out.println("input in bytes: ");
        for (byte b : fileContents) {
            System.out.print(b + " ");
            input.add(b);
        }
        System.out.println();


        encode(input);
        System.out.println("Binary after encoding: ");
        System.out.println(binary);

        OutputStream outputLZWStream = new BufferedOutputStream(new FileOutputStream("./src/archive.lzw"));
        ArrayList<Byte> encoding = new ArrayList<>();
        //while (binary.length() % 8 != 0)
        //    binary += "0";
        for (int i = 0; i < binary.length(); i += 8) {
            if (i + 8 >= binary.length()) {
                String currentByte = binary.substring(i);
                System.out.println("dsadas  " + currentByte);
                while (currentByte.length() < 8)
                    currentByte = currentByte + "0";
                Byte bb = getByteFromBinary(currentByte);
                //System.out.println(bb);
                encoding.add(bb);
                break;
            }
            String currentByte = binary.substring(i, i + 8);
            //System.out.println(currentByte);
            Byte bb = getByteFromBinary(currentByte);
            //System.out.println(bb);
            encoding.add(bb);
        }
        for (Byte b : encoding) {
            outputLZWStream.write(b);
        }
        outputLZWStream.close();

        binary = "";

        byte[] inputArchive =  Files.readAllBytes(Paths.get("./src/archive.lzw"));
        for (byte b : inputArchive) {
            //System.out.println(b + " " + getBinaryFromByte(b));
            binary += getBinaryFromByte(b);
            //System.out.println(getBinaryFromByte(b) + " " + b);
        }

        System.out.println("Binary after reading: ");
        System.out.println(binary);

        System.out.println("Encoded in bytes: ");
        ArrayList<Byte> output = decode();
        coddingClass.makeDict("abacabadabacabae");
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream("./src/output.txt"));
        for (Byte b : output) {
            outputStream.write(b);
        }
        outputStream.close();
    }
}