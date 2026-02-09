package seniorproject.bankifycore.web.v1;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug")
public class DebugAuthController {

    @GetMapping("/me")
    public Map<String, Object> me(Authentication auth) {
        if (auth == null)
            return Map.of("auth", null);

        return Map.of(
                "principal", String.valueOf(auth.getPrincipal()),
                "authorities", auth.getAuthorities().toString(),
                "authenticated", auth.isAuthenticated());
    }
}
