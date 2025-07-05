package securecodingexamples.filedelete.pathmanipulation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@SpringBootApplication
@RestController
public class DeleteController {

    private static final Logger logger = Logger.getLogger(DeleteController.class.getName());
    private static final String TEMP_DIRECTORY = System.getProperty("java.io.tmpdir");
    private static final String UPLOAD_DIRECTORY = TEMP_DIRECTORY + File.separator + "Uploads";
    private static final String[] ALLOWED_EXTENSIONS = {"txt", "pdf"};
    private static final Pattern FILENAME_REGEX_PATTERN = Pattern.compile("[a-zA-Z0-9-_]+");

    public static void main(String[] args) {
        SpringApplication.run(DeleteController.class, args);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> deleteFile(@RequestParam(name = "filename", required = true) String filename) {
        try {
            logger.info("delete file method started");

            if (filename == null || filename.isEmpty() || !isValidName(filename)) {
                logger.warning("Invalid filename requested");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid filename");
            }

            if (!isValidExtension(filename)) {
                logger.warning("Invalid file extension");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file extension");
            }

            filename = validFilename(filename);
            Path filePath = Paths.get(UPLOAD_DIRECTORY).resolve(filename).normalize();

            if (!filePath.startsWith(Paths.get(UPLOAD_DIRECTORY))) {
                logger.warning("Potential path traversal attempt detected: " + filename);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Path Traversal Attempt Detected");
            }

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                logger.warning("File not found or not readable: " + filename);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found or not readable");
            }

            logger.info("File found: " + filename + " at " + filePath);

            Files.delete(filePath);

            logger.info("Deleted file successfully: " + filename);
            return ResponseEntity.ok("File deleted successfully");

        } catch (IOException e) {
            logger.severe("Error deleting file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting file");
        } catch (Exception e) {
            logger.severe("Error deleting file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting file");
        }
    }


    //Logic to validate just the extension part of the filename
    private static boolean isValidExtension(String filename) {
        if(!filename.contains(".")){
            return false;
        }
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        for (String ext : ALLOWED_EXTENSIONS) {
            if (ext.equals(extension)) {
                return true;
            }
        }
        return false;
    }

    //Logic to Validate just the name part of the filename
    private static boolean isValidName(String filename) {
        if(!filename.contains(".")){
            return false;
        }
        String name = filename.substring(0, filename.lastIndexOf("."));
        return name.matches(FILENAME_REGEX_PATTERN.pattern());
    }

    //Logic to return the valide Filename (after validating filename and extension)
    private static String validFilename(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        String name = filename.substring(0, dotIndex);
        name = name.replaceAll("[^a-zA-Z0-9-_]", "_");
        String extension = filename.substring(dotIndex + 1).toLowerCase();
        extension = extension.replaceAll("[^a-zA-Z0-9]", "");
        return name + "." + extension;
    }
}
