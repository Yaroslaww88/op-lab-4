import java.io.*;
import java.util.*;

public class Main {

    public static void encode(ArrayList<Byte> inputBytes, MyWriter writer) {
        int tableSize =  257;
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

        ArrayList<Byte> currentBytes = new ArrayList<>();
        currentBytes.add(inputBytes.get(0));
        for (int i = 1; i < inputBytes.size(); i++) {
            Byte b = inputBytes.get(i);

            ArrayList<Byte> tempCurrentBytes = new ArrayList<>(currentBytes);
            tempCurrentBytes.add(b);

            if (table.containsKey(tempCurrentBytes)) {
                currentBytes.add(b);
            } else {
                /**
                 * Get value from key-value string and convert in to binary according to maxWordLength
                 */
                BitArray bitArray = table.get(currentBytes);

                String binaryString = Utils.getBinaryWithGivenWordLength(wordLength, bitArray.getIntegerValue());
                writer.write(binaryString);

                ArrayList<Byte> arrayToPut = new ArrayList<>(currentBytes);
                arrayToPut.add(b);

                table.put(arrayToPut, new BitArray(wordLength, tableSize));

                /**
                 * recalculate word length 8 => 9 => 10 => ... bits
                 */
                tableSize++;
                wordLength = Utils.calcWordLength(tableSize);

                currentBytes.clear();
                currentBytes.add(b);
            }
        }
        BitArray bitArray = table.get(currentBytes);
        String binaryString = Utils.getBinaryWithGivenWordLength(wordLength, bitArray.getIntegerValue());
        writer.write(binaryString);
        writer.endWriteStream();
    }



    public static void decode(MyToken token, MyWriter writer) {
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

        boolean isZeroIndex = true;
        ArrayList<Byte> currentBytes = new ArrayList<>();
        Integer oldToken = token.getNextToken(wordLength);
        writer.write(table.get(oldToken));
        for (Byte b : table.get(oldToken))
            currentBytes.add(b);
        Byte C = table.get(oldToken).get(0); //TODO rename
        while (true) {
            //Skip first symbol
            if (isZeroIndex) {
                isZeroIndex = false;
                continue;
            }

            Integer currentToken = token.getNextToken(wordLength);
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

            writer.write(currentBytes);

            C = currentBytes.get(0);
            ArrayList<Byte> arrayToPut = new ArrayList<>();
            for (Byte b : table.get(oldToken))
                arrayToPut.add(b);
            arrayToPut.add(C);

            table.put(tableSize, arrayToPut);

            /**
             * recalculate word length 8 => 9 => 10 => ... bits
             */
            tableSize++;
            wordLength = Utils.calcWordLength(tableSize + 1);

            oldToken = currentToken;
        }
        writer.endWriteStream();
    }

    public static void main(String[] args) throws IOException {

        String filename = "./files/input.txt";
        MyReader reader = new MyReader();
        ArrayList<Byte> inputBytes = reader.readBytes(filename);

        MyWriter writer = new MyWriter("./files/archive.lzw");

        encode(inputBytes, writer);

        ArrayList<Byte> archiveBytes = reader.readBytes("./files/archive.lzw");

        writer = new MyWriter("./files/output.txt");

        MyToken token = new MyToken(archiveBytes);

        decode(token, writer);
    }
}