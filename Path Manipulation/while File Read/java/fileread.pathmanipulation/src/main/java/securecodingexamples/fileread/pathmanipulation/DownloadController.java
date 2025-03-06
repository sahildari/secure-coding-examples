package securecodingexamples.fileread.pathmanipulation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    @RequestMapping("/download/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            logger.info("Download request received for: " + filename);

            if (filename==null || !isValidName(filename)) {
                logger.warning("Invalid filename requested");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            if (filename==null || !isValidExtension(filename)) {
                logger.warning("Invalid file extension ");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            if (isValidName(filename) && isValidExtension(filename)) {
                filename = validFilename(filename);
                Path filePath = Paths.get(UPLOAD_DIRECTORY).resolve(filename).normalize();
                Resource resource = new UrlResource(filePath.toUri());

                if (!resource.exists() || !resource.isReadable()) {
                    logger.warning("File not found or not readable: " + filename);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }

                logger.info("File found: " + filename + " at " + filePath);
                logger.info("Downloading file: " + resource);
                return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
            }
            logger.warning("Invalid filename requested");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        catch (Exception e) {
            logger.severe("Error downloading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private static boolean isValidName(String filename) {
        String name = filename.split("\\.", 2)[0];
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
