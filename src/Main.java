import java.io.*;
import java.util.*;

import static java.lang.System.exit;

public class Main {

    public static void encode(MyToken token, MyWriter writer) throws Exception {
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
        try {
            currentBytes.add(token.getNextByte());
            //System.out.println(currentBytes);
        } catch (Exception ex) {
            throw new Exception("Empty file!");
        }
        boolean isFirst = true;
        while (true) {
            if (isFirst) {
                isFirst = false;
                continue;
            }

            if (!token.hasNextByte())
                break;

            //System.out.println(tableSize);

            Byte b = token.getNextByte();

            ArrayList<Byte> tempCurrentBytes = new ArrayList<>(currentBytes);
            tempCurrentBytes.add(b);

            //System.out.println(currentBytes);
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

        HashMap<Integer, ArrayList<Byte>> table = new HashMap<Integer, ArrayList<Byte>>();

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
        currentBytes.addAll(table.get(oldToken));
//        for (Byte b : table.get(oldToken))
//            currentBytes.add(b);
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
                currentBytes.addAll(table.get(oldToken));
//                for (Byte b : table.get(oldToken))
//                    currentBytes.add(b);
                currentBytes.add(C);
            } else {
                currentBytes = new ArrayList<>();
                currentBytes.addAll(table.get(currentToken));
//                for (Byte b : table.get(currentToken))
//                    currentBytes.add(b);
            }

            writer.write(currentBytes);
            //System.out.println(tableSize);

            C = currentBytes.get(0);
            ArrayList<Byte> arrayToPut = new ArrayList<>(table.get(oldToken));
//            for (Byte b : table.get(oldToken))
//                arrayToPut.add(b);
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

    public static void main(String[] args) throws Exception {

//        String filename = "./files/input.exe";
//        MyReader reader = new MyReader(filename);
//        MyToken token = new MyToken(reader);
//
//        MyWriter writer = new MyWriter("./archive.lzw");
//        encode(token, writer);
//        System.out.println("Compression done... Archive can be found at ./archive.lzw");
//
//        filename = "./archive.lzw";
//        reader = new MyReader(filename);
//        token = new MyToken(reader);
//
//        filename = "./files/output.txt";
//        writer = new MyWriter(filename);
//
//        decode(token, writer);
//        System.out.println("Decompression done... Output file can be found at " + filename);

        String action = args[0];

        switch (action) {
            case "compress":
                String filename = args[1];
                MyReader reader = new MyReader(filename);
                MyToken token = new MyToken(reader);

                MyWriter writer = new MyWriter("./archive.lzw");
                encode(token, writer);
                System.out.println("Compression done... Archive can be found at ./archive.lzw");
                break;
            case "decompress":
                filename = args[1];
                reader = new MyReader(filename);
                token = new MyToken(reader);

                filename = args[2];
                writer = new MyWriter(filename);

                decode(token, writer);
                System.out.println("Decompression done... Output file can be found at " + filename);
                break;
        }
    }
}