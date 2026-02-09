package seniorproject.bankifycore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BankifyCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankifyCoreApplication.class, args);

        var enc = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        String hash = "$2a$10$jfpefG9M6SPglB063NOfhujZBXL2rAUlfxcJc7/zSMlQYqd/aSlwG";
        System.out.println(enc.matches("111111", hash));
    }

}
