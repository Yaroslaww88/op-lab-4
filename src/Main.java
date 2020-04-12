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

    public static void decode(ArrayList<Byte> inputBytes) {
        int tableSize =  256;
        int wordLength = 8; //Word length in bits

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
            table.put(arr, new BitArray(wordLength, k));
            k++;
        }

        /**
         * init word length and table size
         */
        wordLength = 9;

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

            if (table.containsKey(tempCurrentBytes)) {
                currentBytes.add(b);
            } else {
                System.out.print(table.get(currentBytes).getValue() + " ");
                binary += table.get(currentBytes).getValue() + " "; //TODO remove (only for testing)
                currentBytes.add(b);

                //TODO: fix (do not create copy of array each time)
                ArrayList<Byte> arrayToPut = new ArrayList<>();
                for (Byte b1 : currentBytes)
                    arrayToPut.add(b1);

                table.put(arrayToPut, new BitArray(wordLength, tableSize));
                tableSize++;
                wordLength = calcWordLength(tableSize);

                currentBytes.clear();
                currentBytes.add(b);
            }
        }
        //TODO fix (do not print last byte without it)
        currentBytes.clear();
        currentBytes.add(inputBytes.get(inputBytes.size() - 1));
        System.out.println(table.get(currentBytes).getValue());
        binary += table.get(currentBytes).getValue() + " "; //TODO remove (only for testing)
    }

    public static void encode() {
        int tableSize =  256;
        int wordLength = 8; //Word length in bits

        TreeMap<BitArray, ArrayList<Byte>> table = new TreeMap<BitArray, ArrayList<Byte>>();

        Integer k = 0;
        for (Byte i = 0; i < 127 ; i++) {
            ArrayList<Byte> arr = new ArrayList<>();
            arr.add(i);
            table.put(new BitArray(wordLength, k), arr);
            k++;
        }

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
        BitArray oldToken = new BitArray(wordLength, bytes.get(0));
        Byte C = 0; //TODO rename
        for (Integer currentByte : bytes) {
            //Skip first symbol
            if (isZeroIndex) {
                isZeroIndex = false;
                continue;
            }

            BitArray currentToken = new BitArray(wordLength, currentByte);

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
            table.put(new BitArray(calcWordLength(tableSize), tableSize), arrayToPut);
            //System.out.println();
            //System.out.println("PUT " + tableSize + " " + arrayToPut.toString());
            tableSize++;
            oldToken = currentToken;
        }

        System.out.println();
    }


    public static void main(String[] args) throws IOException {

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
        decode(input);
        System.out.println("Decoded in binary: ");
        System.out.println(binary);
        System.out.println("Encoded in bytes: ");
        encode();
    }
}