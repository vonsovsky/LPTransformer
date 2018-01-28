package output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileOperator {

    public static void writeTo(String fileName, String content) {
        Path path = Paths.get("./" + fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"))) {
            writer.write(content);
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

}
