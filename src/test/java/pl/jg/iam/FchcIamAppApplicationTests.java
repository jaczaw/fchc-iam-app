package pl.jg.iam;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

@Slf4j
@SpringBootTest
class FchcIamAppApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void verifyHashPassword() {

        veryfyPassword(new BCryptPasswordEncoder(),
                "jaczaw",
                "$2a$10$sV7MFA2OgpeRiXgb3.mAVeWn2egn4BUMfmtDcmEjbwwYJGyffETgK");
        veryfyPassword(new SCryptPasswordEncoder(),
                "jaczaw",
                "$e0801$cJHJTBlF5zLg1UE4tUMovDAGnYUkoR0aVdbkiP99y+eRMTIiPqIE0CfM4fJcv3ZcOqoQQYkWQCE/xj/owahfuw==$6LEVv1XZKQVUEzDnhcbb+v7vQhGPPGidbKNVW93yTvE=");
    }

    public void veryfyPassword(PasswordEncoder passwordEncoder, String password, String hashPassword) {
        log.info(String.format("Typ Crypt: %s", passwordEncoder.getClass().getSimpleName()));
        log.info(String.format("Hasło: %s , Haslo ponownie zakodowane: %s", password, passwordEncoder.encode(password)));
        log.info(String.format("Hash do porównania: %s", hashPassword));
        log.info(String.format("Czy poprawne?: %s", passwordEncoder.matches(password, hashPassword)));
    }


}
