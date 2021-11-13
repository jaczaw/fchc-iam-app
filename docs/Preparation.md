# Preparation

1. Dodaj zależności Web, JPA, MySQL oraz Security z sekcji Zależnośc
2. Dodawanie dodatkowych zależności

```xml
<!-- Do pracy z tokenami sieciowymi Json (JWT) -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>

        <!-- Dla Java 8 Obsługa daty/czasu -->
<dependency>
<groupId>com.fasterxml.jackson.datatype</groupId>
<artifactId>jackson-datatype-jsr310</artifactId>
<version>2.13.0</version>
</dependency>
```
