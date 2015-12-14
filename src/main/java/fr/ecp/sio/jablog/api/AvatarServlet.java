package fr.ecp.sio.jablog.api;

import com.google.appengine.tools.cloudstorage.*;
import com.google.gson.JsonObject;
import fr.ecp.sio.jablog.data.UsersRepository;
import fr.ecp.sio.jablog.model.User;

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
    protected String doPost(HttpServletRequest req) throws ServletException, IOException, ApiException {

        User user_auth = getAuthenticatedUser(req);

        JsonObject json = getJsonRequestBody(req);
        // The json must contain a picture field which value is the path of the file to upload
        if (json == null || !(json.has("picture"))){
            throw new ApiException(400, "invalidRequest", "Invalid JSON body");
        }

        String unique = UUID.randomUUID().toString();

        GcsFilename fileName = new GcsFilename(BUCKET_NAME, unique +".jpg");
        GcsFileOptions options = new GcsFileOptions.Builder()
                .mimeType("image/jpg")
                .acl("public-read")
                .build();
        GcsOutputChannel outputChannel = gcsService.createOrReplace(fileName, options);

        File file = new File(json.get("picture").getAsString());
        byte[] buffer = new byte[BUFFER_SIZE];

        try (OutputStream ops = Channels.newOutputStream(outputChannel); FileInputStream fis = new FileInputStream(file)) {
            int bytesRead = fis.read(buffer);
            while (bytesRead != -1) {
                ops.write(buffer, 0, bytesRead);
                bytesRead = fis.read(buffer);
            }
        }

        String storageURL = "http://storage.googleapis.com/" + BUCKET_NAME + "/" + unique+".jpg";

        user_auth.avatar = storageURL;
        UsersRepository.saveUser(user_auth);

        return storageURL;

    }
}
