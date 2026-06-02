package hrd.com.hrms.util;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileUtil {

    private FileUtil() {}

    // Target directory to save uploaded files on your system
    private static final String UPLOAD_DIR = "uploads/";

    public static String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // Create directory if it doesn't exist
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate a clean, unique name (e.g., e7d4f3b1-a2c3..._avatar.png)
        String originalFilename = file.getOriginalFilename();
        String uniqueFilename = UUID.randomUUID().toString() + "_" +
                (originalFilename != null ? originalFilename.replaceAll("\\s+", "_") : "file");

        // Copy file stream into the target folder
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return the saved string identifier to store in the database string column
        return uniqueFilename;
    }
}