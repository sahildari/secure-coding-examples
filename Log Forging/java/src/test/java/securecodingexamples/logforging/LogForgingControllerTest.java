package securecodingexamples.logforging;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LogForgingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void testSanitizeInput(){
        mockMvc.perform(MockMvcTester.MockMvcRequestBuilder.post("/submit")
                .param("name", "name")
                .param("comment", "comment")
                .andExpect(MockMvcResultMatchers.status().isOK())
                .andExpect(MockMvcResultMatchers.content().string("Greeting name thanks for you comment comment")));
    }

}
