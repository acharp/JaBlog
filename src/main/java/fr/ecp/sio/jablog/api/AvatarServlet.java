package fr.ecp.sio.jablog.api;

import com.google.appengine.tools.cloudstorage.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.channels.Channels;
import java.util.UUID;

/**
 * Created by charpi on 13/12/15.
 */
public class AvatarServlet extends JsonServlet{

    private final GcsService gcsService = GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());

    private static final int BUFFER_SIZE = 2 * 1024 * 1024;
    private static final String BUCKET_NAME = "jablog_images";

    @Override
    protected String doPost(HttpServletRequest req) throws ServletException, IOException {

        String unique = UUID.randomUUID().toString();

        GcsFilename fileName = new GcsFilename(BUCKET_NAME, unique +".jpg");
        GcsFileOptions options = new GcsFileOptions.Builder()
                .mimeType("image/jpg")
                .acl("public-read")
                .build();
        GcsOutputChannel outputChannel = gcsService.createOrReplace(fileName, options);
        OutputStream ops = Channels.newOutputStream(outputChannel);

        Runtime.getRuntime().exec("chmod 666 file");
        //FilePermission permission = new FilePermission("<<ALL FILES>>", "read,write");
        File file = new File("/home/charpi/IdeaProjects/JaBlog/src/main/webapp/bojack.jpg");
        //file.setWritable(true);
        //file.setReadable(true);
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[BUFFER_SIZE];

        try {
            int bytesRead = fis.read(buffer);
            while (bytesRead != 1) {
                ops.write(buffer, 0, bytesRead);
                bytesRead = fis.read(buffer);
            }
        }
        finally {
            fis.close();
            ops.close();
        }

        return null;

    }
}
