package pl.jg.iam.domain.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum RoleName {
    ROLE_GUEST("ROLA-GOSC"),
    ROLE_USER("ROLA-UZYTKOWNIK"),
    ROLE_SUPER_USER("ROLA-SUPER-UZYTKOWNIK"),
    ROLE_ADMIN("ROLA-ADMINISTRATOR"),
    ROLE_SUPER_ADMIN("ROLA-SUPER-ADMINISTRATOR");


    private String nazwaRoli;

    RoleName(String nazwaRoli) {
        this.nazwaRoli = nazwaRoli;
    }


    @JsonCreator
    public static RoleName decode(final String nazwaRoli) {
        return Stream.of(RoleName.values())
                .filter(targetEnum -> targetEnum.nazwaRoli.equals(nazwaRoli))
                .findFirst()
                .orElse(ROLE_GUEST);
    }

    @JsonValue
    public String getNazwaRoli() {
        return nazwaRoli;
    }
}