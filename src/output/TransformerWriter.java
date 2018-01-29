package output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TransformerWriter implements Writer {

    private String fileName;

    private FileWriter fw = null;
    private BufferedWriter bw = null;

    public TransformerWriter(String fileName) {
        this.fileName = fileName;
    }

    public void addString(String line) {
        try {
            bw.write(line);
            bw.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void addStringNoNewline(String line) {
        try {
            bw.write(line);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void open() {
        try {
            fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        try {
            if (bw != null) {
                bw.close();
            }

            if (fw != null) {
                fw.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
