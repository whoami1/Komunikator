package pl.entities.response;

/**
 * Created by Micha³ Wrzesieñ on 2015-05-22.
 */
public class FileMessage implements Message {
    private byte[] file;
    private String recipient;

    public FileMessage(String recipient, byte[] file) {
        this.recipient = recipient;
        this.file = file;
    }

    @Override
    public String getRecipiant() {
        return recipient;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}
