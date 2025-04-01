package uk.ac.ucl.model;

import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class ImageHandler {
    private static final String IMAGES_DIRECTORY = "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "assets" + File.separator + "images";


    public ImageHandler() {
        File imagesDirectory = new File(IMAGES_DIRECTORY);
        if(!imagesDirectory.exists()) {
            if(!imagesDirectory.mkdirs()) {
                System.out.println("//ERROR: Images Directory not Created.");
            }
        }
    }


    public String saveImage(Part image) {
        UUID imageID = UUID.randomUUID();
        if (image != null && image.getSize() > 0) {
            String filePath = getImageDirectory(image);

            File file = new File(filePath);
            try (InputStream imageContent = image.getInputStream()) {
                Files.copy(imageContent, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return filePath;
        }
        return null;
    }


    public String getImageDirectory(Part image) {
        return IMAGES_DIRECTORY + File.separator + image.getSubmittedFileName();
    }
}
