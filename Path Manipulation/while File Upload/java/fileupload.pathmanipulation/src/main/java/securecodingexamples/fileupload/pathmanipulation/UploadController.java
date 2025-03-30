package securecodingexamples.fileupload.pathmanipulation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@SpringBootApplication
@RestController
public class UploadController{
    
    private static final Logger logger = Logger.getLogger(UploadController.class.getName());
    private static final String TEMP_DIRECTORY = System.getProperty("java.io.tmpdir");
    private static final String UPLOAD_DIRECTORY = TEMP_DIRECTORY + File.separator + "Uploads";
    private static final String[] ALLOWED_EXTENSIONS = {"txt", "pdf"};
    private static final Pattern FILENAME_REGEX_PATTERN = Pattern.compile("[a-zA-Z0-9-_]+");
    
    public static void main(String[] args) {
        File uploadDir = new File(UPLOAD_DIRECTORY);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        SpringApplication.run(UploadController.class, args);
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            logger.info("uploadFile() method started");
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No selected file");
            }
            
            String filename = file.getOriginalFilename();
            if (filename == null || filename.isEmpty() || !isValidName(filename)) {
                logger.warning("Invalid Filename");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Filename");
            }

            if (filename == null || filename.isEmpty() || !isValidExtension(filename)) {
                logger.warning("Invalid File Extension");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Extension");
            }

            if(isValidName(filename) && isValidExtension(filename)){
                logger.info("Valid Filename and Extension");
                String validFilename = validFilename(filename);
                validFilename = getUniqueFilename(validFilename);
                File destFile = new File(Paths.get(UPLOAD_DIRECTORY, validFilename).toString());
                file.transferTo(destFile);
                logger.info("File uploaded successfully: " + validFilename);
                return ResponseEntity.ok("File Uploaded Successfully");
            }
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
            
        } catch (IOException e) {
            logger.severe("File upload error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
         } catch (Exception e){
            logger.severe("File upload error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
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

    //Logic to return the valide Filename (after validating)
    private static String validFilename(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        String name = filename.substring(0, dotIndex);
        name = name.replaceAll("[^a-zA-Z0-9-_]", "_");
        String extension = filename.substring(dotIndex + 1).toLowerCase();
        extension = extension.replaceAll("[^a-zA-Z0-9]", "");
        return name + "." + extension;
    }

    //Logic to return the unique Filename, so the file with same filename will not be overwritten
    private static String getUniqueFilename(String filename) {
        String name = filename.split("\\.", 2)[0];
        String extension = filename.split("\\.", 2)[1];
        File file = new File(Paths.get(UPLOAD_DIRECTORY, filename).toString());

        int count = 1;
        while (file.exists()) {
            String newFilename = name + "_" + count + "." + extension;
            file = new File(Paths.get(UPLOAD_DIRECTORY, newFilename).toString());
            count++;
        }
        return file.getName();
    }
}
