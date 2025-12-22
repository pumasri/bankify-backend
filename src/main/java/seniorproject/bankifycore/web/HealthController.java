package seniorproject.bankifycore.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public String health() {
        return "OK";
    }

    @GetMapping("/api/protected-test")
    public String protectedTest() {
        return "You are authenticated 🎉";
    }

}
