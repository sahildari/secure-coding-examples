package securecodingexamples.fileupload.pathmanipulation;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String UPLOAD_DIRECTORY = System.getProperty("java.io.tmpdir") + File.separator + "Uploads";

    @BeforeAll
    static void setup() {
        File uploadDir = new File(UPLOAD_DIRECTORY);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
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

    // ✅ Test: Successful File Upload
    @Test
    @Order(1)
    void testFileUploadSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "valid.txt", "text/plain", "Sample file content".getBytes()
        );

        mockMvc.perform(multipart("/uploadFile")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string("File Uploaded Successfully"));

        File uploadedFile = new File(Paths.get(UPLOAD_DIRECTORY, "sample.txt").toString());
        Assertions.assertTrue(uploadedFile.exists(), "Uploaded file should exist in the directory.");
    }

    // ✅ Test: Empty File Upload
    @Test
    @Order(2)
    void testEmptyFileUpload() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "emptyfile.txt", "text/plain", new byte[0]
        );

        mockMvc.perform(multipart("/uploadFile")
                .file(emptyFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No selected file"));
    }

    // ✅ Test: Invalid File Extension
    @Test
    @Order(3)
    void testInvalidFileExtension() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "malicious.exe", "application/octet-stream", "Malicious content".getBytes()
        );

        mockMvc.perform(multipart("/uploadFile")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Extension"));
    }

    // ✅ Test: Invalid Filename Format
    @Test
    @Order(4)
    void testInvalidFilenameFormat() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "../../escape.txt", "text/plain", "Potential path traversal attempt".getBytes()
        );

        mockMvc.perform(multipart("/uploadFile")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Extension"));
    }

    // ✅ Test: Duplicate Filename Handling
    @Test
    @Order(5)
    void testDuplicateFilename() throws Exception {
        // First file
        MockMultipartFile file1 = new MockMultipartFile(
                "file", "duplicate.txt", "text/plain", "Original content".getBytes()
        );
        mockMvc.perform(multipart("/uploadFile").file(file1).contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        // Second file with the same name
        MockMultipartFile file2 = new MockMultipartFile(
                "file", "duplicate.txt", "text/plain", "Duplicate content".getBytes()
        );
        mockMvc.perform(multipart("/uploadFile").file(file2).contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string("File Uploaded Successfully"));

        // Ensure unique filename logic worked
        File uploadedFile1 = new File(Paths.get(UPLOAD_DIRECTORY, "duplicate.txt").toString());
        File uploadedFile2 = new File(Paths.get(UPLOAD_DIRECTORY, "duplicate_1.txt").toString());

        Assertions.assertTrue(uploadedFile1.exists(), "First uploaded file should exist");
        Assertions.assertTrue(uploadedFile2.exists(), "Second uploaded file should be renamed and exist");
    }
}