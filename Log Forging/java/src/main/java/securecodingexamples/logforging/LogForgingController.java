package securecodingexamples.logforging;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogForgingController {

    private static final Logger logger = LoggerFactory.getLogger(LogForgingController.class);
    private static final Pattern ILLEGAL_CHARS = Pattern.compile("[^a-zA-Z0-9\\-_ ]");

    @PostMapping("/submit")
    public ResponseEntity<String> logForging(@RequestParam("name") String name,
                                             @RequestParam("comment") String comment){
        name = sanitizeInput(name);
        comment = sanitizeInput(comment);

        //Logging sanitized input only
        logger.info("User {} commented: {}", name, comment);

        String response = "Greetings "
                + name
                + " thanks for your comment "
                + comment;
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private String sanitizeInput(String input){
        input = input.replaceAll("\\n", "_").replaceAll("\\r", "_").replaceAll("\\t", "_");
        return ILLEGAL_CHARS.matcher(input).replaceAll("_");
    }
}