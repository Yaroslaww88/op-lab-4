import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MyReader {

    MyReader() {
    }

    public ArrayList<Byte> readBytes(String filename) throws IOException {
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
}
