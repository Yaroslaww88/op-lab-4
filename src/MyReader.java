import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class MyReader {

    private String filename;
    private Long start = 0L;

    MyReader(String filename) {
        this.filename = filename;
    }

    public ArrayList<Byte> readBytes() throws IOException {
        try {
            byte[] fileContents = Files.readAllBytes(Paths.get(filename));
            ArrayList<Byte> input = new ArrayList<Byte>();
            for (byte b : fileContents) {
                input.add(b);
            }
            return input;
        } catch (IOException ex) {
            throw ex;
        }
    }


    public ArrayList<Byte> readBytes(Integer _count) throws IOException {

        File file = new File(filename);
        Long length = file.length();
        if (start >= length)
            return new ArrayList<Byte>();
        long count = Math.min(new Long(_count), length - start);
        //System.out.println(length + " " + count);
        byte[] array = new byte[(int)count];

        //ArrayList<Byte> array = new ArrayList<>();
        InputStream in = new FileInputStream(file);
        in.skip(start);
        long offset = 0;
        while (offset < count) {

            try {
                int tmp = in.read(array, (int) offset, (int) (count - offset));
                offset += tmp;
            } catch (Exception ex) {
                //System.out.println(ex);
            }


        }
        in.close();

        ArrayList<Byte> result = new ArrayList<>();
        for (byte b : array) result.add(b);
        //System.out.println(_count + " " + result.size());
        start += result.size();
        return result;
    }
}
