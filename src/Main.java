import com.sun.java.swing.plaf.windows.WindowsTextAreaUI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BinaryOperator;

public class Main {

    static String binary = "";

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

    public static void encode(ArrayList<Byte> inputBytes) {
        int tableSize =  256;
        int wordLength = 9; //Word length in bits (9 because we have 8bit alphabet)

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
        //TODO fix upper limit to 256
        for (Byte i = 0; i < 127 ; i++) {
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
                binary += binaryString  + " "; //TODO remove (only for testing)

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
        currentBytes.clear();
        currentBytes.add(inputBytes.get(inputBytes.size() - 1));
        //TODO ended
        //System.out.println(getBinaryWithGivenWordLength(tableSize, table.get(currentBytes)));
        /**
         * Get value from key-value string and convert in to binary according to maxWordLength
         */
        BitArray bitArray = table.get(currentBytes);
        //maxWordLength = Math.max(maxWordLength, bitArray.length);
        String binaryString = getBinaryWithGivenWordLength(wordLength, bitArray.getIntegerValue());
        binary += binaryString  + " "; //TODO remove (only for testing)
    }

    public static void decode() {
        int tableSize =  256;
        int wordLength = 9; //Word length in bits

        TreeMap<Integer, ArrayList<Byte>> table = new TreeMap<Integer, ArrayList<Byte>>();

        Integer k = 0;
        for (Byte i = 0; i < 127; i++) {
            ArrayList<Byte> arr = new ArrayList<>();
            arr.add(i);
            table.put(k++, arr);
        }

        /**
         * max word length of outputted token (only increase)
         * if we get BitArray from string with WordLength > maxWordLength => maxWordLength := WordLength
         */
        Integer maxWordLength = 8;

        String byteString = "";
        ArrayList<Integer> bytes = new ArrayList<>(); //Contains codes not bytes
        for (char c : binary.toCharArray()) {
            if (c != ' ') {
                byteString += c;
            } else {
                Integer b = Integer.parseInt(byteString, 2);
                byteString = "";

                bytes.add(b);
            }
        }

        /**
         * init word length and table size
         */
        wordLength = 9;

        boolean isZeroIndex = true;
        //System.out.print(table.get(bytes.get(0)).get(0) + " ");
        ArrayList<Byte> currentBytes = new ArrayList<>();
        Integer oldToken = bytes.get(0);
        Byte C = 0; //TODO rename
        for (Integer currentToken : bytes) {
            //Skip first symbol
            if (isZeroIndex) {
                isZeroIndex = false;
                continue;
            }

            //BitArray currentToken = new BitArray(wordLength, currentByte);

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

            for (Byte b : currentBytes)
                System.out.print(b + " ");
            //System.out.print(currentBytes.toString() + " ");
            C = currentBytes.get(0);
            ArrayList<Byte> arrayToPut = new ArrayList<>();
            for (Byte b : table.get(oldToken))
                arrayToPut.add(b);
            arrayToPut.add(C);
            table.put(tableSize, arrayToPut);
            //System.out.println();
            //System.out.println("PUT " + tableSize + " " + arrayToPut.toString());
            tableSize++;
            oldToken = currentToken;
        }

        System.out.println();
    }


    public static void main(String[] args) throws IOException {

        System.out.println(getBinaryWithGivenWordLength(8, 10));

        String filename = "./src/input.txt";

        byte[] fileContents =  Files.readAllBytes(Paths.get(filename));
        ArrayList<Byte> input = new ArrayList<Byte>();
        System.out.println("input in bytes: ");
        for (byte b : fileContents) {
            System.out.print(b + " ");
            input.add(b);
        }
        System.out.println();


        System.out.println("Decoded in bytes: ");
        encode(input);
        System.out.println("Decoded in binary: ");
        System.out.println(binary);
        System.out.println("Encoded in bytes: ");
        decode();
    }
}