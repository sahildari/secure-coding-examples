package securecodingexamples.fileread.pathmanipulation;

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
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@SpringBootApplication
@RestController
public class DownloadController {

    private static final Logger logger = Logger.getLogger(DownloadController.class.getName());
    private static final String TEMP_DIRECTORY = System.getProperty("java.io.tmpdir");
    private static final String UPLOAD_DIRECTORY = TEMP_DIRECTORY + File.separator + "Uploads";
    private static final String[] ALLOWED_EXTENSIONS = {"txt", "pdf"};
    private static final Pattern FILENAME_REGEX_PATTERN = Pattern.compile("[a-zA-Z0-9-_]+");

    public static void main(String[] args) {
        SpringApplication.run(DownloadController.class, args);
    }

    @RequestMapping("/download")
    @ResponseBody
    public ResponseEntity<?> downloadFile(@RequestParam(name = "filename", required = true) String filename) {
        try {
            logger.info("Download file method started");

            if(filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
                logger.warning("Invalid filename requested");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid filename");
            }

            if (filename==null || filename.isEmpty() || !isValidName(filename)) {
                logger.warning("Invalid filename requested");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid filename");
            }

            if (filename==null || filename.isEmpty() || !isValidExtension(filename)) {
                logger.warning("Invalid file extension");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file extension");
            }

            if (isValidName(filename) && isValidExtension(filename)) {
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
                logger.info("Downloading file: " + resource);
                return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
            }
            logger.warning("Invalid filename requested");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid filename");
        }
        catch (IOException e) {
            logger.severe("Error downloading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error downloading file");
        }
        
        catch (Exception e) {
            logger.severe("Error downloading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error downloading file");
        }
    }

    private static boolean isValidName(String filename) {
        String name = filename.split("\\.", 2)[0];
        if(name.isEmpty()) {
            return false;
        }
        return name.matches(FILENAME_REGEX_PATTERN.pattern());
    }

    private static String validFilename(String filename) {
        String name = filename.split("\\.", 2)[0];
        String extension = filename.split("\\.", 2)[1];
        return name + "." + extension;
    }

    private static boolean isValidExtension(String filename) {
        String extension = filename.split("\\.", 2)[1];
        for (String ext : ALLOWED_EXTENSIONS) {
            if (ext.equals(extension)) {
                return true;
            }
        }
        return false;
    }
}
