package securecodingexamples.pathmanipulation;

//import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String index() {
        return "index.html";
    }
    // public static void main(String[] args) {
    //     SpringApplication.run(HomeController.class, args);
    // }
}