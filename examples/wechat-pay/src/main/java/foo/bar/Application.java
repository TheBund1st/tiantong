package foo.bar;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Application {

    @PostMapping("/api/notifications")
    public void handle(@RequestBody String command) {
        System.out.println(command);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
