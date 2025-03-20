package securecodingexamples.unrestricted.fileupload;

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
                "file", "sample.pdf", "application/pdf", "%PDF-1.4 sample content".getBytes()
        );

        mockMvc.perform(multipart("/uploadFile")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string("File Uploaded Successfully"));

        Assertions.assertTrue(new File(Paths.get(UPLOAD_DIRECTORY, "sample.pdf").toString()).exists());
    }

    // ✅ Test: Empty File Upload
    @Test
    @Order(2)
    void testEmptyFileUpload() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile("file", "emptyfile.pdf", "application/pdf", new byte[0]);

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
        MockMultipartFile file = new MockMultipartFile("file", "malicious.exe", "application/octet-stream", "Malicious content".getBytes());

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
        MockMultipartFile file = new MockMultipartFile("file", "../../escape.pdf", "application/pdf", "Potential path traversal attempt".getBytes());

        mockMvc.perform(multipart("/uploadFile")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Filename"));
    }

    // ✅ Test: Duplicate Filename Handling
    @Test
    @Order(5)
    void testDuplicateFilename() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("file", "duplicate.pdf", "application/pdf", "%PDF-1.4 content".getBytes());
        mockMvc.perform(multipart("/uploadFile").file(file1).contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        MockMultipartFile file2 = new MockMultipartFile("file", "duplicate.pdf", "application/pdf", "%PDF-1.4 second content".getBytes());
        mockMvc.perform(multipart("/uploadFile").file(file2).contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string("File Uploaded Successfully"));

        Assertions.assertTrue(new File(Paths.get(UPLOAD_DIRECTORY, "duplicate.pdf").toString()).exists());
        Assertions.assertTrue(new File(Paths.get(UPLOAD_DIRECTORY, "duplicate_1.pdf").toString()).exists());
    }

    // ✅ Test: Invalid Magic Number
    @Test
    @Order(6)
    void testInvalidMagicNumber() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "invalid.pdf", "application/pdf", "Fake PDF content".getBytes()
        );

        mockMvc.perform(multipart("/uploadFile")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("File upload failed"));
    }
}
