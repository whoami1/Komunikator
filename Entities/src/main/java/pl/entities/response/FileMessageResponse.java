package pl.entities.response;

/**
 * Created by Micha³ Wrzesieñ on 2015-05-22.
 */
public class FileMessageResponse implements MessageResponse
{
    private byte[] file;
    private String recipient;
    private String filename;

    public FileMessageResponse(String recipient, byte[] file, String filename)
    {
        this.recipient = recipient;
        this.file = file;
        this.filename = filename;
    }

    @Override
    public String getRecipiant()
    {
        return recipient;
    }

    public byte[] getFile()
    {
        return file;
    }

    public void setFile(byte[] file)
    {
        this.file = file;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }
}
