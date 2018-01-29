package output;

public interface Writer {

    public void open();

    public void addString(String line);

    public void addStringNoNewline(String line);

    public void close();

}
