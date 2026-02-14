package seniorproject.bankifycore.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import seniorproject.bankifycore.consants.ApiPaths;

@RestController
public class HealthController {

    @GetMapping(ApiPaths.HEALTH)
    public String health() {
        return "This is working awesome";
    }
}
