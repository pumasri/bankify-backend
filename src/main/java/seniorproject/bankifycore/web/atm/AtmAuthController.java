package seniorproject.bankifycore.web.atm;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import seniorproject.bankifycore.dto.atm.AtmLoginRequest;
import seniorproject.bankifycore.dto.atm.AtmLoginResponse;
import seniorproject.bankifycore.service.atm.AtmAuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/atm")
public class AtmAuthController {
    private final AtmAuthService atmAuthService;

    @PostMapping("/login")
    public AtmLoginResponse login(@RequestBody AtmLoginRequest req) {
        return atmAuthService.login(req);
    }
}
