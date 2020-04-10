import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static void myCompress(ArrayList<Byte> inputBytes) {
        int tableSize =  127;

        TreeMap<ArrayList<Byte>, Integer> table = new TreeMap<ArrayList<Byte>, Integer>(new Comparator<ArrayList<Byte>>() {
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
        for (Byte i = 0; i < 127 ; i++) {
            /*byte[] b = new byte [1];
            b[0] = i;*/
            ArrayList<Byte> arr = new ArrayList<>();
            arr.add(i);
            table.put(arr, k);
            k++;
            //TABLE.put(ByteBuffer.allocate(4).putInt(i).array(), i);
        }

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
                System.out.print((table.get(currentBytes) - 97) + " ");
                currentBytes.add(b);

                //TODO: fix (do not create copy of array each time)
                ArrayList<Byte> arrayToPut = new ArrayList<>();
                for (Byte b1 : currentBytes)
                    arrayToPut.add(b1);

                table.put(arrayToPut, tableSize);
                tableSize++;

                currentBytes.clear();
                currentBytes.add(b);
            }
        }
    }


    public static void main(String[] args) throws IOException {

        String filename = "src/input.txt";

        byte[] fileContents =  Files.readAllBytes(Paths.get(filename));
        ArrayList<Byte> input = new ArrayList<Byte>();
        for (byte b : fileContents) {
            System.out.print(b + " ");
            input.add(b);
        }
        System.out.println();

        myCompress(input);
    }
}