package securecodingexamples.fileread.pathmanipulation;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DownloadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String UPLOAD_DIRECTORY = System.getProperty("java.io.tmpdir") + File.separator + "Uploads";

    @BeforeAll
    static void setup() throws Exception {
        File uploadDir = new File(UPLOAD_DIRECTORY);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Create sample files for testing
        createSampleFile("validfile.txt", "Sample file content");
        createSampleFile("anotherfile.pdf", "PDF file content");
    }

    @AfterEach
    void cleanup() {
        File uploadDir = new File(UPLOAD_DIRECTORY);
        if (uploadDir.exists()) {
            for (File file : uploadDir.listFiles()) {
                file.delete();
            }
        }
    }

    private static void createSampleFile(String filename, String content) throws Exception {
        File file = new File(Paths.get(UPLOAD_DIRECTORY, filename).toString());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes());
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Test: Successful Download - Valid File
    @Test
    @Order(1)
    void testSuccessfulDownload() throws Exception {
        mockMvc.perform(get("/download?filename=validfile.txt"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"validfile.txt\""))
                .andExpect(content().string("Sample file content"));
    }

    // ✅ Test: File Not Found
    @Test
    @Order(2)
    void testFileNotFound() throws Exception {
        mockMvc.perform(get("/download?filename=nonexistentfile.txt"))
                .andExpect(status().isNotFound());
    }

    // ✅ Test: Invalid Filename Format
    @Test
    @Order(3)
    void testInvalidFilename() throws Exception {
        mockMvc.perform(get("/download?filename=../../escape.txt"))
                .andExpect(status().isBadRequest()); 
    }

    // ✅ Test: Invalid File Extension
    @Test
    @Order(4)
    void testInvalidFileExtension() throws Exception {
        createSampleFile("invalidfile.exe", "Malicious content");
        mockMvc.perform(get("/download?filename=invalidfile.exe"))
                .andExpect(status().isBadRequest());
    }
}