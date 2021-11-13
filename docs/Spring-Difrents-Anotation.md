# Spring-Difrents-Anotation

Różnice między @Component, @Repository, @Controller i @Service

### 1. @Component

Jest to adnotacja do stereotypu ogólnego przeznaczenia wskazująca, że ​​klasa jest komponentem Spring.

Co jest specjalnego @Component
`<context:component-scan>` tylko skanuje *@Component* i nie szuka w ogóle @Controller,@Service i @Repository. Są one
skanowane, ponieważ same są oznaczone adnotacją @Component.

Wystarczy spojrzeć na @Controller, @Servicei @Repository definicje adnotacji:

```json
@Component
public @interface Service {
  ….
}


@Component
public @interface Repository {
….
}


@Component
public @interface Controller {
…
}
```

Tak więc, to nie znaczy, że jest źle @Controller, @Servicea @Repository to specjalne typy adnotacji
@Component. `<context:component-scan>` podnosi je i rejestruje ich kolejne klasy jako fasole, tak jakby były oznaczone
@Component.

Skanowane są również adnotacje typu specjalnego, ponieważ same są opatrzone adnotacją @Component, co oznacza, że ​​są
również @Components. Jeśli zdefiniujemy własną niestandardową adnotację i dodamy do niej adnotację @Component, zostanie
ona również zeskanowana za pomocą `<context:component-scan>`

### 2. @Repository

Ma to na celu wskazanie, że klasa definiuje repozytorium danych.

Co jest specjalnego w @Repository?

Oprócz wskazywania, że ​​jest to konfiguracja oparta na adnotacjach , @Repository zadaniem programu jest przechwytywanie
wyjątków specyficznych dla platformy i ponowne zgłaszanie ich jako jednego z ujednoliconych niesprawdzonych wyjątków
Springa. W tym celu mamy `PersistenceExceptionTranslationPostProcessor`, które musimy dodać w kontekście naszej
aplikacji Spring w następujący sposób:

`<bean class="org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor"
/>`
Ten postprocesor ziarna dodaje doradcę do każdego ziarna, które jest oznaczone adnotacjami, @Repository dzięki czemu
wszelkie wyjątki specyficzne dla platformy są przechwytywane, a następnie ponownie zgłaszane jako jeden z
niesprawdzonych wyjątków dostępu do danych w Springu.

### 1. @Kontroler

@Controller Adnotacja wskazuje, że dana klasa pełni rolę kontrolera. @Controller Adnotacja działa jako stereotypu dla
klasy adnotacjami, wskazując swoją rolę.

Co jest specjalnego w @Controller?

Nie możemy zamienić tej adnotacji na adnotację podobną do @Service lub @Repository, nawet jeśli wyglądają tak samo.
Dyspozytor skanuje klasy opatrzone adnotacjami @Controller i wykrywa metody opatrzone @RequestMapping adnotacjami w
nich. Możemy używać @RequestMappingo n/in tylko tych metod, których klasy są opatrzone adnotacjami @Controller i które
NIE będą działać z @Component, @Service, @Repository itd...

Uwaga: Jeśli klasa jest już zarejestrowana jako fasoli za pomocą dowolnej metody alternatywnej, jak przez @Bean lub za
pośrednictwem @Component, @Service etc ... adnotacje, a następnie @RequestMapping mogą być zbierane, jeśli klasa jest
również opatrzone @RequestMapping adnotacją. Ale to inny scenariusz.

### 1. @Service

@Service ziarna zawierają logikę biznesową i metody wywoływania w warstwie repozytorium.

Co jest specjalnego w @Service?

Oprócz tego, że jest używany do wskazywania, że trzyma logikę biznesową, w tej adnotacji nie ma nic innego; ale kto wie,
Spring może w przyszłości dodać jakieś dodatkowe wyjątkowe.

Co jeszcze?

Podobny do powyższego, w przyszłości Spring może dodać specjalne funkcje dla @Service, @Controller i @Repository na
podstawie swoich konwencjach kolejności nakładania. Dlatego zawsze warto szanować konwencję i używać jej zgodnie z
warstwami.
