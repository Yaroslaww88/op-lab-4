import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

public class inputtoFile {
    public static void FileInputBinary(String file, byte[] input) throws IOException {
        File name = new File(file);
        try (FileOutputStream stream = new FileOutputStream(name)) {
            stream.write(input);
        }
    }
}
