package securecodingexamples.fileread.pathmanipulation;

import org.junit.jupiter.api.Test;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class FileUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testFileUploadSuccess() throws Exception {
            MockMultipartFile mockFile = new MockMultipartFile(
                "file",                               // Field name in request
                "testfile.txt",                       // Original file name
                "text/plain",                         // Content type
                "Sample file content".getBytes()      // File content
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/upload")
                .file(mockFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("File uploaded successfully")));
    }

    @Test
    void testEmptyFileUpload() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "emptyfile.txt", "text/plain", new byte[0]
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/upload")
                .file(emptyFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("File is empty")));
    }

    @Test
    void testFileUploadServerError() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "testfile.txt", "text/plain", "Sample file content".getBytes()
        );

        // Mocking server error by setting an invalid path
        MockMvc mockMvcWithError = MockMvcBuilders.standaloneSetup(new FileUploadController() {
            @Override
            public ResponseEntity<?> handleFileUpload(MultipartFile file) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to upload file: Simulated error");
            }
        }).build();

        mockMvcWithError.perform(MockMvcRequestBuilders.multipart("/api/files/upload")
                .file(mockFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to upload file")));
    }
}
