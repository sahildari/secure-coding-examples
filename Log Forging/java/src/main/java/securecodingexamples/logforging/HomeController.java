package securecodingexamples.logforging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HomeController {
    public static void main(String[] args) {
        SpringApplication.run(LogForgingController.class, args);
    }
}
