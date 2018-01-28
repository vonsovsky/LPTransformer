package transformation;

import data.Position;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class LPTransformerFileHandler {

    private List<Position> items;
    private String fileName;
    private LPTransformer instance = null;

    private FileWriter fw = null;
    private BufferedWriter bw = null;

    public LPTransformerFileHandler(List<Position> items, String fileName) {
        this.items = items;
        this.fileName = fileName;
    }

    public LPTransformer getInstance() {
        if (instance == null) {
            createFileHandler();
            instance = new LPTransformer(items, bw);
        }
        return instance;
    }

    private void createFileHandler() {
        try {
            fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void closeFileHandler() {
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
