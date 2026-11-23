# Guida al progetto `libreria_rhotechnology`

Documentazione didattica del progetto: architettura, flusso completo di una richiesta, scelte fatte e
istruzioni per replicare lo stesso impianto in un altro progetto Java/Spring Boot.

---

## 1. Cos'è questo progetto

È un'**applicazione a layer** realizzata con **Spring Boot 4**:

- un solo artefatto deployabile (un JAR)
- un solo database MySQL condiviso da tutte le entità
- i package `controller` / `service` / `repository` / `entity` / `mapper` / `dto` sono **layer tecnici**, non
  confini di dominio
- le chiamate tra "moduli" (Libro → Autore → Nazionalità) sono semplici chiamate Java in-process

Espone una **REST API JSON** per gestire libri, autori, generi, case editrici, nazionalità e magazzino,
protetta da **JWT** con utenti salvati su database.

### Stack effettivo

| Componente | Versione / nota |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.2 (`spring-boot-starter-parent`) |
| Web | `spring-boot-starter-webmvc` (in Boot 4 sostituisce `spring-boot-starter-web`) |
| Persistenza | `spring-boot-starter-data-jpa` + Hibernate + `mysql-connector-j` |
| Validazione | `spring-boot-starter-validation` (Jakarta Bean Validation) |
| Sicurezza | `spring-boot-starter-security` + `spring-boot-starter-security-oauth2-resource-server` |
| JWT | `jjwt-api` / `jjwt-impl` / `jjwt-jackson` 0.12.6 |
| Documentazione API | `springdoc-openapi-starter-webmvc-ui` 3.2.0 (Swagger UI) |
| JSON | **Jackson 3** (`tools.jackson.*`), quello di default in Boot 4 |
| Lombok | presente nel `pom.xml` con scope `annotationProcessor`, ma **non usato** nel codice |

> Nota: in Spring Boot 4 i nomi degli starter e il package di Jackson cambiano rispetto a Boot 3.
> Esempio: in `JwtAuthenticationEntryPoint` l'import è `tools.jackson.databind.json.JsonMapper`.

---

## 2. Struttura dei package

```
com.rhotechnologylibri.libreria_rhotechnology
├── LibreriaRhotechnologyApplication.java   # @SpringBootApplication (entry point)
├── config/
│   ├── OpenApiConfig.java                  # info + schema "bearerAuth" per Swagger
│   └── SpringSecurityConfig.java           # filter chain, CORS, PasswordEncoder
├── controller/                             # livello HTTP: routing, status, DTO
├── dto/
│   ├── request/                            # ciò che il client invia
│   └── response/                           # ciò che il server restituisce
├── entity/                                 # mappatura JPA delle tabelle
├── exception/                              # eccezioni custom + @RestControllerAdvice
├── mapper/                                 # Entity <-> DTO (manuale, @Component)
├── pagination/Paging.java                  # helper per costruire Pageable
├── repository/                             # interfacce JpaRepository
├── security/                               # JWT: service, filter, entry point
└── service/                                # logica applicativa + @Transactional
```

**Regola d'oro del layering**: le dipendenze vanno sempre verso il basso.

```
Controller ──► Service ──► Repository ──► Entity
     │             │
     └──► DTO ◄────┘  (il Mapper fa da ponte)
```

Un `Controller` non deve mai parlare con un `Repository`, e un `Service` non deve mai ricevere o
restituire una `HttpServletRequest`/`ResponseEntity`.

---

## 3. Modello dati

### Entità e relazioni

| Entità | Tabella | Relazioni |
|---|---|---|
| `Libro` | `libri` | `@ManyToOne` → `Genere`; `@ManyToMany` → `Autore` (tabella `libri_autori`) |
| `Autore` | `autori` | `@ManyToOne` → `Nazionalita`; `@ManyToMany(mappedBy = "autori")` ← `Libro` |
| `Genere` | `generi` | — |
| `CasaEditrice` | `case_editrici` | — |
| `Nazionalita` | `nazionalita` | — |
| `Magazzino` | `magazzino` | `@ManyToOne` → `Libro` (obbligatorio), `@ManyToOne` → `CasaEditrice` (opzionale) |
| `User` | `users` | — |

```
Nazionalita 1 ──── N Autore N ──── N Libro N ──── 1 Genere
                      │                │
                      │                └──── 1..N Magazzino N ──── 1 CasaEditrice
                      └──────── libri_autori (join table)
```

### Punti importanti sulle entità

1. **`@ManyToOne` sempre `LAZY`** (default per `@ManyToOne` è `EAGER`: qui è forzato a `LAZY`).
   Con `LAZY` il DB non viene interrogato finché non si legge davvero la relazione: la decisione di
   caricarla spetta al repository (`@EntityGraph`) o al mapper.
2. **`@ManyToMany` con lato proprietario esplicito**: `Libro` ha `@JoinTable`, `Autore` ha
   `mappedBy = "autori"`.
3. **Metodi di sincronizzazione bidirezionale** su `Libro`:

   ```java
   public void addAutore(Autore autore) {
       autori.add(autore);
       autore.getLibri().add(this);   // aggiorna anche il lato inverso
   }
   ```

   Servono perché, in memoria, JPA vede due collezioni distinte: se aggiorni solo un lato, l'altro
   resta disallineato fino al reload. Il service usa *solo* questi metodi (`replaceAutori`).
4. **`ddl-auto=validate`**: Hibernate **non crea** le tabelle, si limita a verificare che lo schema
   corrisponda. Il DB deve esistere già (vedi §11).

### Schema SQL di riferimento

```sql
CREATE DATABASE libreria_db;

CREATE TABLE generi (
  id   INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE nazionalita (
  id   INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE case_editrici (
  id   INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE autori (
  id              INT AUTO_INCREMENT PRIMARY KEY,
  nome            VARCHAR(100) NOT NULL,
  cognome         VARCHAR(100) NOT NULL,
  data_nascita    DATE NULL,
  id_nazionalita  INT NULL,
  CONSTRAINT fk_autori_nazionalita FOREIGN KEY (id_nazionalita) REFERENCES nazionalita(id)
);

CREATE TABLE libri (
  id        INT AUTO_INCREMENT PRIMARY KEY,
  titolo    VARCHAR(255) NOT NULL,
  anno      INT NULL,
  id_genere INT NULL,
  CONSTRAINT fk_libri_genere FOREIGN KEY (id_genere) REFERENCES generi(id)
);

CREATE TABLE libri_autori (
  id_libro  INT NOT NULL,
  id_autore INT NOT NULL,
  PRIMARY KEY (id_libro, id_autore),
  CONSTRAINT fk_la_libro  FOREIGN KEY (id_libro)  REFERENCES libri(id),
  CONSTRAINT fk_la_autore FOREIGN KEY (id_autore) REFERENCES autori(id)
);

CREATE TABLE magazzino (
  id               INT AUTO_INCREMENT PRIMARY KEY,
  id_libro         INT NOT NULL,
  quantita         INT NOT NULL DEFAULT 1,
  id_casa_editrice INT NULL,
  CONSTRAINT fk_mag_libro FOREIGN KEY (id_libro) REFERENCES libri(id),
  CONSTRAINT fk_mag_casa  FOREIGN KEY (id_casa_editrice) REFERENCES case_editrici(id)
);

CREATE TABLE users (
  id       BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL
);
```

> Il progetto usa `PhysicalNamingStrategyStandardImpl`: i nomi delle colonne sono **esattamente** quelli
> dichiarati (`@Column(name = ...)`) o, in assenza, il nome del campo Java. Non c'è conversione
> automatica `camelCase → snake_case` (tranne dove è scritta a mano: `data_nascita`, `id_genere`, ...).

---

## 4. Il flusso completo di una richiesta (il cuore della guida)

Esempio: `GET /api/libri/1` con header `Authorization: Bearer <token>`.

```
1. Tomcat (embedded) riceve la richiesta HTTP
        │
2. DelegaFilterChain di Spring Security
        ├── CorsFilter  → se è una preflight OPTIONS, risponde e si ferma
        ├── JwtAuthenticationFilter  → legge l'header, valida il token, popola il SecurityContext
        │      (se il token manca/non è valido NON blocca: lascia proseguire non autenticato)
        └── AuthorizationFilter  → decide se l'utente può accedere a /api/libri/1
               ├── negato → JwtAuthenticationEntryPoint → 401 JSON + STOP
               └── permesso → prosegue
        │
3. DispatcherServlet instrada verso LibroController.findById(1)
        │
4. Spring MVC converte il path {id} in Integer (@PathVariable)
        │
5. LibroController chiama libroService.findById(1)
        │
6. LibroService apre una transazione read-only (@Transactional(readOnly = true))
        │
7. LibroRepository.findById(1) → Hibernate genera SELECT, ritorna Optional<Libro>
        │      (entità in stato "managed": relazioni LAZY non ancora caricate)
        │
8. LibroMapper.toResponse(entity) legge titolo, anno, genere, autori
        │      → ogni accesso LAZY non ancora inizializzato genera una SELECT aggiuntiva
        │        (== il classico problema N+1: qui mitigato da @EntityGraph e batch fetch)
        │
9. Jackson serializza LibroResponseDTO in JSON
        │
10. La transazione viene chiusa (commit/rollback non serve: era readOnly)
        │
11. ResponseEntity.ok(dto) → HTTP 200 + body JSON
```

### Cosa succede quando qualcosa va storto

| Situazione | Chi se ne accorge | Risultato |
|---|---|---|
| id inesistente | `Optional.empty()` nel service | lancia `ResourceNotFoundException` |
| body non valido (`@Valid`) | Spring MVC, **prima** del controller | `MethodArgumentNotValidException` |
| token assente/scaduto | `AuthorizationFilter` | `JwtAuthenticationEntryPoint` → 401 |
| UNIQUE violato al flush | Hibernate al commit | `DataIntegrityViolationException` (409) |
| genere usato da libri (delete) | controllo esplicito nel service | `ResourceInUseException` (409) |
| tutto il resto | `@ExceptionHandler(Exception.class)` | 500 con body uniforme |

Tutte queste eccezioni convergono in **`GlobalExceptionHandler`** (`@RestControllerAdvice`), che produce
sempre lo stesso formato:

```json
{
  "timestamp": "2026-11-23T10:20:00",
  "status": 404,
  "error": "Not Found",
  "message": "Libro con id 99 non esiste",
  "path": "/api/libri/99"
}
```

Questo è il motivo per cui i controller non hanno **nessun** `try/catch`: l'errore è una
preoccupazione trasversale, gestita in un punto solo.

---

## 5. Il contratto API

Tutti gli endpoint tranne `/auth/login`, `/auth/register` e Swagger richiedono il token.

| Metodo | Path | Body | Risposta | Note |
|---|---|---|---|---|
| POST | `/auth/register` | `AuthRequestDTO` | 201 `"Utente registrato"` | pubblico |
| POST | `/auth/login` | `AuthRequestDTO` | 200 `{"token": "..."}` | pubblico |
| GET | `/auth/me` | — | 200 `{"username": "..."}` | utile per testare il token |
| GET | `/api/libri?page=0&size=10` | — | 200 `Page<LibroResponseDTO>` | ordinato per `titolo` |
| GET | `/api/libri/{id}` | — | 200 `LibroResponseDTO` | 404 se non esiste |
| POST | `/api/libri` | `LibroRequestDTO` | 201 `LibroResponseDTO` | |
| PUT | `/api/libri/{id}` | `LibroRequestDTO` | 200 `LibroResponseDTO` | |
| DELETE | `/api/libri/{id}` | — | 204 | |
| GET | `/api/autori?page=0&size=10` | — | 200 `Page<AutoreResponseDTO>` | ordinato per `cognome`, `nome` |
| GET | `/api/autori/{id}` | — | 200 | |
| POST/PUT/DELETE | `/api/autori[/{id}]` | `AutoreRequestDTO` | 201 / 200 / 204 | |
| GET | `/api/generi` | — | 200 `List<GenereResponseDTO>` | non paginato |
| GET | `/api/generi/{id}` | — | 200 | |
| GET | `/api/generi/{id}/libri` | — | 200 `List<LibroResponseDTO>` | endpoint "di navigazione" |
| POST/PUT/DELETE | `/api/generi[/{id}]` | `GenereRequestDTO` | 201 / 200 / 204 | delete protetto se in uso |
| GET | `/api/case-editrici` | — | 200 `List<...>` | |
| GET | `/api/nazionalita` | — | 200 `List<...>` | |
| GET | `/api/magazzino?page=0&size=10` | — | 200 `Page<MagazzinoResponseDTO>` | ordinato per `id` |
| POST/PUT/DELETE | `/api/magazzino[/{id}]` | `MagazzinoRequestDTO` | 201 / 200 / 204 | |

### Esempio: creare un libro con genere e autori "al volo"

```json
POST /api/libri
{
  "titolo": "Il nome della rosa",
  "anno": 1980,
  "nomeGenere": "Romanzo storico",
  "idAutori": [3],
  "nuoviAutori": [
    { "nome": "Umberto", "cognome": "Eco", "idNazionalita": 1 }
  ]
}
```

Il `LibroService`:
1. mappa i campi scalari (`titolo`, `anno`) con `LibroMapper.toEntity`
2. chiama `GenereService.resolve(idGenere, nomeGenere)`: il genere viene cercato per nome
   (`IgnoreCase`) e **creato se assente**
3. chiama `AutoreService.resolve(idAutori, nuoviAutori)`: gli id devono esistere, i nuovi autori vengono
   riusati per nome+cognome o creati
4. sostituisce l'intera associazione autori con `replaceAutori`
5. salva e rimappa in DTO

Questa è la tecnica **"resolve dei riferimenti"**: il client può usare id oppure nomi, e il service
centralizza la regola (incluso il controllo "non puoi indicare entrambi" → 400).

---

## 6. I pattern, uno per uno (con il perché)

### 6.1 Controller sottile + `ResponseEntity`

```java
@GetMapping("/{id}")
public ResponseEntity<LibroResponseDTO> findById(@PathVariable Integer id) {
    return ResponseEntity.ok(libroService.findById(id));
}
```

- Il controller **non contiene logica**: traduce HTTP → chiamata al service → HTTP.
- `ResponseEntity` è **facoltativo**: un metodo che restituisce il DTO nudo produce già `200 OK`.
  Qui è usato per **uniformità** (tutti i metodi lo usano) e per poter controllare status/header in
  futuro (`Location` sui POST, `ETag`, `304`, ...).
- Il `DELETE` invece è `void` + `@ResponseStatus(HttpStatus.NO_CONTENT)`: non c'è body, quindi
  `ResponseEntity` non aggiungerebbe nulla.
- **Regola**: in un controller scegli uno stile e mantienilo per tutti i metodi. Il misto è la cosa
  peggiore, perché chi legge non sa se una differenza è voluta.

### 6.2 DTO request/response: mai esporre le entità

Due famiglie di classi:

- **`*RequestDTO`** — ciò che il client invia. Contiene solo campi scalari e riferimenti per id/nome
  (es. `idGenere`, `nomeGenere`), **mai** entità JPA. Per le relazioni preferisce gli id: deserializzare
  un'`Entità` da JSON significa permettere al client di scrivere qualsiasi campo, incluse le
  associazioni.
- **`*ResponseDTO`** — ciò che il server restituisce. Contiene solo campi "piatti" e sotto-DTO.

**Perché è obbligatorio:**

1. **Sicurezza**: l'entità `User` non ha una `password` che deve uscire; un DTO la esclude per costruzione.
2. **Cicli infiniti**: `Libro.autori` → `Autore.libri` → `Libro.autori`... Jackson andrebbe in
   ricorsione. Il DTO spezza il ciclo scegliendo *un* lato.
3. **Disaccoppiamento**: puoi rinominare una colonna o cambiare strategia JPA senza rompere il contratto
   HTTP.
4. **`LAZY`**: esponendo l'entità, la serializzazione tocchi relazioni lazy fuori dalla transazione →
   `LazyInitializationException`. Con i DTO la conversione avviene **dentro** il service/transazione.

### 6.3 Mapper manuali (`@Component`)

```java
@Component
public class AutoreMapper {
    private final NazionalitaMapper nazionalitaMapper;   // i mapper si compongono

    public AutoreResponseDTO toResponse(Autore entity) {
        if (entity == null) return null;
        AutoreResponseDTO dto = new AutoreResponseDTO(
                entity.getId(), entity.getNome(), entity.getCognome(), entity.getDataNascita());
        dto.setNazionalita(nazionalitaMapper.toResponse(entity.getNazionalita()));
        return dto;
    }
}
```

Convenzioni usate in tutti i mapper:

- `toResponse(entity)` → DTO di risposta (null-safe)
- `toResponseList(entities)` → lista, `List.of()` se l'input è `null`
- `toEntity(request)` → nuova entità con i **soli campi scalari**
- `updateEntity(entity, request)` → aggiorna l'entità esistente (per la PUT)

Le **relazioni non si risolvono nel mapper**: le risolve il service (`resolve(...)`), perché richiedono
accesso al database. Il mapper resta una funzione pura di conversione.

Vantaggio rispetto a MapStruct: zero dipendenze, zero magia, completamente debuggabile. Svantaggio:
codice ripetitivo (accettabile in progetti piccoli/medi, ed è ottimo per studiare).

### 6.4 Service con `@Transactional`

```java
@Transactional(readOnly = true)
public LibroResponseDTO findById(Integer id) { ... }

@Transactional
public LibroResponseDTO update(Integer id, LibroRequestDTO request) { ... }
```

- `readOnly = true` sulle letture: Hibernate disattiva il dirty checking (meno lavoro, meno memoria) e
  alcuni DB possono ottimizzare (replica in lettura, ecc.).
- `@Transactional` sui metodi di scrittura garantisce che lettura-modifica-scrittura sia **atomica**.
- **La transazione deve racchiudere anche la mappatura verso il DTO**: è lì che si toccano le relazioni
  `LAZY`. Per questo `toResponse(...)` è chiamato *dentro* i metodi annotati.
- Il service è anche il posto dove applicare la **regola di business**: es. `delete` di un genere
  controlla prima se esistono libri che lo usano e lancia `ResourceInUseException` (409) invece di
  lasciare che il DB generi un errore di foreign key poco leggibile.

### 6.5 Repository: derived query + `@EntityGraph`

```java
public interface LibroRepository extends JpaRepository<Libro, Integer> {

    @Override
    @EntityGraph(attributePaths = "genere")
    Page<Libro> findAll(Pageable pageable);

    boolean existsByGenereId(Integer idGenere);
    List<Libro> findByGenereId(Integer idGenere);
}
```

- **Derived query**: `existsByGenereId`, `findByNomeIgnoreCase`,
  `findFirstByNomeIgnoreCaseAndCognomeIgnoreCase`. Spring Data genera l'SQL dal nome del metodo.
- **`@EntityGraph`**: senza di esso, la lista di N libri farebbe 1 query per i libri + N query per i
  generi (problema **N+1**). Con `@EntityGraph(attributePaths = "genere")` Hibernate fa una `JOIN` e
  carica i generi in un colpo.
- **Perché solo relazioni `ManyToOne` nell'`EntityGraph`**: aggiungerci una collezione (`autori`)
  trasformerebbe la query in una join con duplicazione di righe, e la paginazione non sarebbe più fatta
  dal database ma **in memoria** (con warning di Hibernate). Le collezioni restano `LAZY` e vengono
  caricate in batch grazie a `hibernate.default_batch_fetch_size=25`.

### 6.6 Paginazione con un helper (`Paging`)

```java
public final class Paging {
    public static Pageable of(int page, int size, Sort sort) {
        return PageRequest.of(
                Math.max(page, 0),
                size <= 0 ? DEFAULT_SIZE : Math.min(size, MAX_SIZE),
                sort);
    }
}
```

Due scopi:

1. **Sicurezza/robustezza**: `size` non può superare 100 (evita `?size=1000000`), `page` negativa
   diventa 0.
2. **Documentazione**: i controller dichiarano `page` e `size` come `@RequestParam` espliciti invece di
   ricevere `Pageable`. Così in Swagger compaiono due campi leggibili, non il blocco di parametri
   generato da Spring per `Pageable`.

L'ordinamento è deciso **nel controller** (`Sort.by("titolo")`, `Sort.by("cognome", "nome")`), non
lasciato in mano al client: nessun `?sort=` arbitrario su campi interni.

### 6.7 Eccezioni custom + `@RestControllerAdvice`

```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, Integer id) {
        super(resource + " con id " + id + " non esiste");
    }
}
```

| Eccezione | HTTP | Quando |
|---|---|---|
| `ResourceNotFoundException` | 404 | id inesistente |
| `BadRequestException` | 400 | richiesta incoerente (es. id + nome insieme, `idLibro` mancante) |
| `DuplicateResourceException` | 409 | UNIQUE violato gestito esplicitamente (username già preso) |
| `ResourceInUseException` | 409 | delete bloccato da riferimenti |
| `UnauthorizedException` | 401 | credenziali errate, token assente in `/auth/me` |
| `DataIntegrityViolationException` | 409 | vincolo DB violato (messaggio generico al client, dettaglio nei log) |
| `Exception` | 500 (o status originale) | rete di sicurezza finale |

Perché le eccezioni custom invece di `ResponseStatusException` in ogni punto:

- il service resta **ignorante di HTTP** (non importa `HttpStatus`)
- lo status è deciso in un solo file
- il JSON di errore è uniforme

Il `catch` finale (`handleGeneric`) ha un trucco: se l'eccezione implementa `ErrorResponse` (le
eccezioni interne di Spring: 404 su path sbagliato, 405 metodo non permesso, 415 media type), conserva
lo status originale invece di degradare a 500.

### 6.8 Doppio lato delle associazioni: `replaceAutori`

```java
private void replaceAutori(Libro libro, List<Autore> autori) {
    new ArrayList<>(libro.getAutori()).forEach(libro::removeAutore);
    autori.forEach(libro::addAutore);
}
```

- Si itera su una **copia** (`new ArrayList<>(...)`): modificare la collezione mentre la si itera
  lancia `ConcurrentModificationException`.
- Si passa sempre dai metodi `add`/`remove` dell'entità per tenere sincronizzati i due lati.
- Semantica PUT: **la lista inviata sostituisce quella esistente**. Lista vuota = libro senza autori.
  Questo è il comportamento REST corretto per una PUT (rappresentazione completa).

---

## 7. Sicurezza (JWT)

### Flusso

```
POST /auth/login {username, password}
        │
        ├── userRepository.findByUsername(...)
        ├── passwordEncoder.matches(raw, hash)     ← BCrypt
        ├── jwtService.generateToken(username)
        └── 200 {"token": "eyJhbGciOi..."}

Richiesta successiva
        │
        ├── header: Authorization: Bearer <token>
        ├── JwtAuthenticationFilter
        │      ├── estrae il subject (username)
        │      ├── verifica firma + scadenza (JwtService)
        │      ├── carica l'utente dal DB (esiste ancora?)
        │      └── SecurityContextHolder.setAuthentication(...)
        └── AuthorizationFilter: /api/** → authenticated() → OK
```

### I tre pezzi

| Classe | Ruolo |
|---|---|
| `JwtService` | crea e verifica i token (`jjwt`). Firma HMAC-SHA con `jwt.secret` |
| `JwtAuthenticationFilter` | `OncePerRequestFilter` eseguito **prima** di `UsernamePasswordAuthenticationFilter`: trasforma il token in un `Authentication`. Se il token è invalido **non lancia**: lascia la richiesta non autenticata e decide la catena |
| `JwtAuthenticationEntryPoint` | risponde **401** con lo stesso JSON di `GlobalExceptionHandler`. Senza di lui Spring userebbe un entry point che risponde **403 con body vuoto** |

### `SpringSecurityConfig` in sintesi

- `csrf` disabilitato: API stateless con token, non c'è sessione né cookie di sessione.
- `cors(Customizer.withDefaults())` **prima** della catena di autenticazione: così le preflight
  `OPTIONS` (che non portano token) non vengono respinte con 401.
- `sessionCreationPolicy(STATELESS)`: nessuna `HttpSession`.
- `permitAll` solo su: `/auth/login`, `/auth/register`, `/swagger-ui/**`, `/v3/api-docs/**`.
- `addFilterBefore(jwtAuthenticationFilter, ...)`.
- **Toggle di sviluppo**: `app.security.enabled=false` salta tutta la parte di autenticazione
  (`anyRequest().permitAll()`). Comodo per provare da Swagger senza fare login; **da non lasciare
  attivo** in produzione.

### CORS

```java
configuration.setAllowedOrigins(allowedOrigins);   // da app.cors.allowed-origins
configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
configuration.setAllowedHeaders(List.of("*"));
configuration.setAllowCredentials(true);
configuration.setMaxAge(3600L);
```

`allowCredentials(true)` richiede `allowedOrigins` **espliciti** (non `"*"`): per questo il valore
arriva dalla property. Il progetto è pensato per un frontend Angular su `localhost:4200`.

### Swagger + JWT

`OpenApiConfig` dichiara lo schema `bearerAuth` e lo applica globalmente: Swagger UI mostra il pulsante
**Authorize**, si incolla il token, e ogni chiamata porta `Authorization`. Sui due endpoint pubblici c'è
`@SecurityRequirements` (vuoto) che toglie il lucchetto e non invia l'header.

---

## 8. Configurazione (`application.properties`) spiegata

```properties
spring.application.name=libreria_rhotechnology

# Connessione MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/libreria_db
spring.datasource.username=root
spring.datasource.password=test

# validate = non tocca lo schema, verifica solo che combaci con le entità
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.use_sql_comments=true

# i nomi di tabella/colonna sono quelli dichiarati, senza conversione automatica
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl

# carica le collezioni LAZY in gruppi da 25 invece di una query per elemento (mitiga l'N+1)
spring.jpa.properties.hibernate.default_batch_fetch_size=25

# serializzazione di Page: forma "stabile" (content + metadati) invece della struttura interna di PageImpl
spring.data.web.pageable.serialization-mode=via_dto

# origini consentite dal frontend
app.cors.allowed-origins=http://localhost:4200,http://127.0.0.1:4200

# interruttore per disattivare l'autenticazione in sviluppo
app.security.enabled=true

# JWT: HS256 richiede un secret di almeno 32 caratteri. In produzione: variabile d'ambiente JWT_SECRET
jwt.secret=${JWT_SECRET:sono.un.segreto.segretoso.1234567890987654321234567890987654321}
jwt.expiration=86400000

# Swagger UI conserva il token dopo il refresh della pagina
springdoc.swagger-ui.persist-authorization=true
```

Note importanti:

- **Mai** un secret di default in produzione: `${JWT_SECRET:...}` significa "usa la variabile
  d'ambiente, altrimenti questo fallback". Il fallback esiste solo per lo sviluppo.
- `ddl-auto=validate` è una scelta **consapevole**: nessuna modifica automatica dello schema, quindi
  nessun rischio di perdita dati. In un progetto reale si aggiunge uno strumento di migrazione
  (Flyway/Liquibase).
- `default_batch_fetch_size` è la contromisura all'N+1 sulle collezioni quando non si può usare
  `@EntityGraph` (perché la paginazione è fatta dal DB).

---

## 9. Swagger / OpenAPI

- UI: `http://localhost:8080/swagger-ui.html`
- JSON: `http://localhost:8080/v3/api-docs`

Come provare:

1. `POST /auth/register` → crea l'utente
2. `POST /auth/login` → copia il `token`
3. **Authorize** → incolla solo il token (senza `Bearer `, lo aggiunge Swagger)
4. chiama gli endpoint `/api/**`

---

## 10. Come avviare

```powershell
# 1. crea il DB e le tabelle (SQL in §3)
# 2. eventuale utente di test
#    (registrabile anche via POST /auth/register)

# 3. avvio (Windows)
.\mvnw.cmd spring-boot:run

# 4. compilazione senza avviare
.\mvnw.cmd -q -o compile
```

Il progetto attualmente **non ha test**: `src/test` contiene solo cartelle vuote, nessun file di test. Le
uniche verifiche sono la compilazione e le prove manuali via Swagger/curl (gli starter `*-test` sono però
già nel `pom.xml`, pronti all'uso).

Esempio `curl` completo:

```bash
# login
TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password123"}' | jq -r .token)

# lista paginata
curl -s "http://localhost:8080/api/libri?page=0&size=10" -H "Authorization: Bearer $TOKEN"

# dettaglio
curl -s "http://localhost:8080/api/libri/1" -H "Authorization: Bearer $TOKEN"

# creazione con relazioni risolte per nome
curl -s -X POST http://localhost:8080/api/libri \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"titolo":"1984","anno":1949,"nomeGenere":"Distopia","nuoviAutori":[{"nome":"George","cognome":"Orwell"}]}'
```

---

## 11. Come replicare questo impianto in un nuovo progetto

Ordine di creazione consigliato (ogni passo dipende dal precedente):

1. **Progetto** — Spring Initializr con: Spring Web, Spring Data JPA, Validation, Security, MySQL Driver.
2. **`application.properties`** — datasource, `ddl-auto=validate`, naming strategy, batch fetch size.
3. **Entità** — una per tabella, `@ManyToOne(fetch = LAZY)`, `@ManyToMany` con lato proprietario, metodi
   `addX`/`removeX` per le associazioni bidirezionali.
4. **Schema SQL** — coerente con le entità (o introduci subito Flyway).
5. **Repository** — `extends JpaRepository<T, Id>`, aggiungi `@EntityGraph` su `findAll(Pageable)` per
   le `ManyToOne` che finiscono nei DTO, poi le derived query man mano che servono.
6. **DTO** — `*RequestDTO` (soli tipi primitivi/id) e `*ResponseDTO` (campi piatti + sotto-DTO).
7. **Mapper** — `@Component`, `toResponse` / `toResponseList` / `toEntity` / `updateEntity`.
8. **Eccezioni** — le 4-5 custom + `GlobalExceptionHandler` con `build(status, message, request)`.
9. **Service** — `@Transactional` giusto, `resolve` per le relazioni, `delete` con i controlli
   "in uso", mappatura DTO dentro il metodo transazionale.
10. **Controller** — solo routing, `@Valid`, `ResponseEntity` coerente.
11. **`Paging`** — helper con limite massimo di `size`.
12. **Sicurezza** — `JwtService`, `JwtAuthenticationFilter`, `JwtAuthenticationEntryPoint`,
    `SpringSecurityConfig` (+CORS), utente + `UserRepository`.
13. **OpenApiConfig** — schema bearer applicato globalmente.

### Checklist "da non dimenticare"

- [ ] Nessuna entità esce dal controller
- [ ] `toResponse(...)` chiamato dentro la transazione
- [ ] `@EntityGraph` solo sulle `ManyToOne`, mai sulle collezioni paginate
- [ ] `size` massimo nella paginazione
- [ ] `DELETE` protetto quando la risorsa è referenziata
- [ ] secret JWT da variabile d'ambiente in produzione
- [ ] CORS con origini esplicite se `allowCredentials=true`
- [ ] un solo stile di ritorno nei controller
- [ ] nessun `try/catch` nei controller per gli errori di business

---

## 12. Punti di attenzione / possibili estensioni (utile per lo studio)

Cose che nel progetto sono **volutamente** in una forma semplice, e come evolverebbero:

1. **Validazione in ingresso** — oggi solo `AuthRequestDTO` ha annotazioni (`@NotBlank`, `@Size`). Gli
   altri `*RequestDTO` non hanno vincoli: `@Valid` nei controller è quindi un no-op e i controlli
   avvengono nel service (`BadRequestException`). Passo successivo naturale: `@NotBlank` su
   `titolo`/`nome`/`cognome`, `@Positive` su `quantita`, e lasciare al service solo le regole di
   coerenza tra campi.
2. **`AuthController` senza service** — parla direttamente con `UserRepository`. È l'unico punto fuori
   dal pattern; un `AuthService` renderebbe tutto uniforme e testabile.
3. **Nessun test** — con `spring-boot-starter-*-test` già nel `pom.xml`, i primi da scrivere sono i test
   del `GlobalExceptionHandler` e del `JwtService`, poi `@WebMvcTest` sui controller con `MockMvc`.
4. **Nessuna migrazione dello schema** — con `ddl-auto=validate` lo schema è gestito a mano. Flyway è
   l'evoluzione standard.
5. **Nessun ruolo/autorizzazione** — chiunque abbia un token può fare tutto. Il passo successivo è
   aggiungere `roles` all'`User`, metterli come claim nel token e usare
   `hasRole("ADMIN")` / `@PreAuthorize`.
6. **`create` non restituisce `Location`** — status 201 corretto, ma l'header con l'URI della risorsa
   creata è la rifinitura standard:

   ```java
   return ResponseEntity.created(URI.create("/api/libri/" + dto.getId())).body(dto);
   ```
7. **Refresh token / revoca** — oggi il token dura 24h e non è revocabile. Con ruoli e logout reale
   servirebbe una blacklist o token a vita breve + refresh.
8. **Password policy** — c'è solo `@Size(max = 72)` (limite BCrypt). Manca un minimo di robustezza.
9. **Segreti nel repo** — `spring.datasource.password` in chiaro nel file: in un progetto reale va in
   variabili d'ambiente o in un secret manager.
10. **Organizzazione per dominio** — se in futuro il progetto crescesse, il passo successivo è
    raggruppare i package per dominio (`libri/`, `autori/`, `auth/`) invece che per layer tecnico, con
    dipendenze controllate tra i gruppi. Questo codice è già pronto per quel passaggio.

---

## 13. Glossario rapido

| Termine | Significato nel progetto |
|---|---|
| **Entity** | classe mappata su tabella (`@Entity`). Vive dentro la transazione |
| **DTO** | oggetto di trasporto per HTTP. Non sa nulla di JPA |
| **Mapper** | converte Entity ↔ DTO. Puro, senza DB |
| **Repository** | interfaccia Spring Data che genera l'SQL dal nome del metodo |
| **Service** | regole di business + confini transazionali |
| **`@Transactional`** | delimita una transazione DB; `readOnly=true` per le letture |
| **N+1** | 1 query per la lista + N query per le relazioni. Si cura con `@EntityGraph` o batch fetch |
| **`@EntityGraph`** | forza l'`EAGER` di relazioni specifiche solo per quella query |
| **Lazy / Eager** | quando JPA carica una relazione: al bisogno o subito |
| **Derived query** | `findByNomeIgnoreCase` → Spring genera la query |
| **`Pageable` / `Page`** | paginazione fatta dal DB / risultato con metadati |
| **`@RestControllerAdvice`** | gestore globale delle eccezioni per tutti i controller |
| **`ResponseEntity`** | wrapper HTTP: status + header + body |
| **JWT** | token firmato che contiene il subject (username); stateless |
| **`SecurityContext`** | dove Spring Security tiene l'utente autenticato della richiesta |
| **CORS** | permesso al browser di chiamare un'origine diversa dalla propria |
| **Applicazione a layer** | una sola applicazione, divisa per strati tecnici |
