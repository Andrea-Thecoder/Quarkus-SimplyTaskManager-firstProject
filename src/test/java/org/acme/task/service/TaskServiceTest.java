package org.acme.task.service;

import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.task.dto.TaskUpdateDTO;
import org.acme.task.mapper.TaskMapper;
import org.acme.task.model.Task;
import org.acme.task.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;


@QuarkusTest
public class TaskServiceTest {

    @Inject
    TaskService taskServiceTest ;  //DI della classe che siamo testando

    @Inject
    TaskMapper taskMapper;

    @Inject
    TaskRepository taskRepository;


    /*private long taskId;

    @BeforeEach //annotation per dire: prima di ogni test esegui questo method.
    @Transactional
    public void setup() {
        // Crea un task iniziale da aggiornare
        Task task = new Task();
        task.setTitle("Initial Title");
        task.setDescription("Initial Description");
        task.setComplete(false);
        task.setCreateAt(LocalDateTime.now());
        task.setUpdateAt(LocalDateTime.now());

        taskRepository.persist(task);
        this.taskId = task.getId();
    }*/

    @Test //questo test deve avere successo
    @Transactional //il test deve rispecchiare tutte le annotation della vera classe/method
    public void updateTaskTest() {
        long taskId = 3;
        //creiamo un fake updateDTO che entra in ingresso
        TaskUpdateDTO updateDTO = new TaskUpdateDTO();
        updateDTO.setTitle("cavolet222ti");
        updateDTO.setDescription("Nsa");
        updateDTO.setComplete(false);

        given() //parte request
                .contentType(ContentType.JSON) //indichimo il tipo di contenuto che dovrebbe entrare in Input.
                .body(updateDTO) //inseriamo il body della request
        .when()
                .put("/task/update/" + taskId)
        .then() //parte response
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("status", is(200)) // Verifica che lo status sia corretto
                .body("data.id", is((int) taskId)) // Controlla che l'ID sia corretto
                .body("data.title", is("Cavolet222ti")) // Controlla che il titolo sia stato aggiornato
                .body("data.description", is("Nsa")) // Controlla che la descrizione sia stata aggiornata
                .body("data.createAt", notNullValue()) // Assicuriamoci che createAt non sia nullo
                .body("data.updateAt", notNullValue()) // Assicuriamoci che updateAt non sia nullo
                .body("data.isComplete", is(false)); // Verifica se isComplete è true


    }


    @Test //test che avrà successo lanciando un errore di illegal argument
    @Transactional
    public void updateTaskNotFoundTest (){
        long invalidId = 80; // non esiste nel db questo ID
        TaskUpdateDTO updateDTO = new TaskUpdateDTO();
        updateDTO.setTitle("Nuovo Titolo");

        given()
                .contentType(ContentType.JSON)
                .body(updateDTO)
        .when()
                .put("/task/update/" + invalidId)
        .then()
                .statusCode(404)
                .body("status", is(404))
                .body("message",is("Resource not found"))
                .body("details", notNullValue());
    }

    @Test //test che avrà successo lanciando un errore di  nosuchfoundt
    @Transactional
    public void updateTaskInvalidIdTest (){
        long invalidId = -1; // ID non valido
        TaskUpdateDTO updateDTO = new TaskUpdateDTO();
        updateDTO.setTitle("Nuovo Titolo");

        given()
                .contentType(ContentType.JSON)
                .body(updateDTO)
                .when()
                .put("/task/update/" + invalidId)
                .then()
                .statusCode(400)
                .body("status", is(400))
                .body("message",is("Validation not work"))
                .body("details", notNullValue());
    }


    @Test //test che avrà successo lanciando un errore nella validazione updateDTO
    @Transactional
    public void updateTaskInputInvalidTest (){
        long invalidId = -1; // ID non valido
        TaskUpdateDTO updateDTO = new TaskUpdateDTO();
        updateDTO.setTitle("");

        given()
                .contentType(ContentType.JSON)
                .body(updateDTO)
                .when()
                .put("/task/update/" + invalidId)
                .then()
                .statusCode(400)
                .body("status", is(400))
                .body("message",is("Validation not work"))
                .body("details", notNullValue());
    }

    //per rendere piu velcoe ed automatizzato il tutto possiamo creare dei method specifici che cfreano i stream di dati (flusso di dati) e poi import nel method,  come segue:

    private static Stream<InputForTest> invalidInputs() {
        return Stream.of(
                // Errore: ID non valido
                new InputForTest(-1, "", "Descrizione", true), // ID non valido, titolo vuoto
                new InputForTest(-2, null, "Descrizione", false), // ID non valido, titolo null
                new InputForTest(0, "Titolo", "Descrizione", true), // ID zero, titolo valido

                // Errore: Titolo vuoto
                new InputForTest(1, "", "Descrizione", true), // ID valido, titolo vuoto

                // Errore: Descrizione vuota
                new InputForTest(4, "Titolo", "", false), // ID valido, titolo valido, descrizione vuota

                // Errore: Titolo troppo lungo
                new InputForTest(5, "Titolo con più di 40 caratteri per testare il limite dei caratteri", "Descrizione", true)
        );


    }

    @ParameterizedTest //indica che è u ntest con parametri in Input
    @MethodSource("invalidInputs") //indica la fonte degli input da dove proviene
    @DisplayName("Test with input: {0}") //annotation per mostrare input corrente.
    @Transactional
    public void updateTaskInputInvalidTest(InputForTest input){
        long id = input.getId();
        String title = input.getTitle();
        String description = input.getDescription();
        Boolean isComplete = input.getComplete();

        TaskUpdateDTO updateDTO = new TaskUpdateDTO();
        updateDTO.setTitle(title);
        updateDTO.setDescription(description);
        updateDTO.setComplete(isComplete);

        //Possiamo farci ridare un oggetto con la response, senza andarla subiato a controllare  con i vari .body , .status etc usando come segue:
        Response response = given()
                        .contentType(ContentType.JSON)
                        .body(updateDTO)
                .when()
                .put("/task/update/" + id)
                .thenReturn(); //restitusice la rispsota senza far i check sopra
        //response è l'oggetto contenente la rispsota creata dop oaver simulato la reuqest nel test
        //controlliamo le asserzioni (assertion = modo per affermare che una certa condizione deve essere soddisfatta in un punto specifico del codice)

        System.out.println("Stiamo all'input avente:" + input.getId());

        //assert al lserve per controlalre tutte le possibili eccezion iche vengono lanciate durantei l test
        assertAll(
                () -> {
                    assertEquals(400,response.getStatusCode());
                    if(id <= 0){
                        assertEquals("Validation not work", response.jsonPath().getString("message"),
                                "The error message should indicate 'Illegal argument' for negative IDs.");
                    } else {
                        assertEquals("Validation not work", response.jsonPath().getString("message"),
                                "The error message should indicate 'Validation not work' for validation fail");
                    }
                },
        () -> assertNotNull(response.jsonPath().getString("details"))
        );
    }


    private static Stream<InputForTest> validInputs() {
        return Stream.of(
                // ID valido (1), titolo e descrizione validi
                new InputForTest(1, "Titolo valido1", "Descrizione valida1", true),
                new InputForTest(1, "Titolo valido2", "Descrizione valida2", false),
                new InputForTest(1, "Titolo valido3", "Descrizione valida3", null),

                // ID valido (2), titolo valido, descrizione null
                new InputForTest(2, "Titolo valido4", null, null),

                // ID valido (3), titolo null, descrizione valida
                new InputForTest(3, null, "Descrizione valida4", null),
                new InputForTest(3, null, "Descrizione valida5", true),

                // ID valido (4), titolo e descrizione null
                new InputForTest(4, null, null, null),
                new InputForTest(4, null, null, false)
        );
    }


    @ParameterizedTest //indica che è u ntest con parametri in Input
    @MethodSource("validInputs") //indica la fonte degli input da dove proviene
    @DisplayName("Test with input: {0}") //annotation per mostrare input corrente.
    @Transactional
    public void updateTaskInputValidTest(InputForTest input) {
        long id = input.getId();
        String title = input.getTitle();
        String description = input.getDescription();
        Boolean isComplete = input.getComplete();

        TaskUpdateDTO updateDTO = new TaskUpdateDTO();
        updateDTO.setTitle(title);
        updateDTO.setDescription(description);
        updateDTO.setComplete(isComplete);

        // Simula la richiesta PUT
        Response response = given()
                .contentType(ContentType.JSON)
                .body(updateDTO)
                .when()
                .put("/task/update/" + id)
                .thenReturn(); // Restituisce la risposta senza controlli

        System.out.println("Stiamo testando l'input con ID: " + id);
        System.out.println(response.getBody().asString());

        // Assert per controllare il successo della risposta
        assertAll(
                () -> assertEquals(200, response.getStatusCode(), "Expected HTTP status 200"),
                () -> {
                    // Accesso ai dati nel campo "data"
                    assertNotNull(response.jsonPath().getString("data.id"), "ID should not be null");
                    assertNotNull(response.jsonPath().getString("data.title"), "Title should not be null");
                    assertNotNull(response.jsonPath().getString("data.description"), "Description should not be null");
                    assertNotNull(response.jsonPath().getString("data.createAt"), "CreatedAt should not be null");
                    assertNotNull(response.jsonPath().getString("data.updateAt"), "UpdatedAt should not be null");
                    assertNotNull(response.jsonPath().getBoolean("data.isComplete"), "IsComplete should not be null");
                }
        );
    }








//classe itnerna che useremo per i test:
static class InputForTest {
    private final long id;
    private final String title;
    private final Boolean isComplete;
    private final String description;
    private String extraField;


    public InputForTest(long id, String title, String description, Boolean isComplete) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isComplete  = isComplete;
    }

    public String getExtraField() {
        return extraField;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getComplete() {
        return isComplete;
    }

    public void setExtraField(String extraField) {
        this.extraField = extraField;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}}

