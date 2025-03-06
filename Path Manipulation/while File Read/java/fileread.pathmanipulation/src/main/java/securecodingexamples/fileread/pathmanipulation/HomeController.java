package securecodingexamples.fileread.pathmanipulation;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private static final String TEMP_DIRECTORY = System.getProperty("java.io.tmpdir");
    private static final String UPLOAD_DIRECTORY = TEMP_DIRECTORY + File.separator + "Uploads";

    @GetMapping("/")
    public String listFiles(Model model) {
        File folder = new File(UPLOAD_DIRECTORY);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        
        List<String> files = Arrays.stream(folder.listFiles())
                .filter(File::isFile)
                .map(File::getName)
                .collect(Collectors.toList());

        model.addAttribute("files", files);
        return "index"; // Renders index.html
    }
    
}
