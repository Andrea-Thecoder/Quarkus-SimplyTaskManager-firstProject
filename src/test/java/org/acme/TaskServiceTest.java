
package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import jakarta.transaction.Transactional;
import org.acme.dto.task.TaskCreateDTO;
import org.acme.dto.task.TaskUpdateDTO;
import org.acme.util.JwtAuthTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.params.provider.Arguments.arguments;


@QuarkusTest
public class TaskServiceTest {

    @Test
    @Transactional
    public void testCreateTaskWithValidData() {
        long currentTimeMillis = System.currentTimeMillis();
        String jwtAuth = JwtAuthTest.loginAndGetJwt();
        TaskCreateDTO taskCreateDTO = new TaskCreateDTO();
        taskCreateDTO.setTitle("New Task Title" + currentTimeMillis);
        taskCreateDTO.setDescription("Task description for a new task.");

        // Esegui la richiesta per creare il task con il token JWT nell'header
        ValidatableResponse response =
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + jwtAuth)
                        .body(taskCreateDTO)
                        .when()
                        .post("/tasks")
                        .then()
                        .log().all()
                        .statusCode(200);

        // Verifica la risposta
        response.body("data.id", notNullValue());
        response.body("data.title", is(taskCreateDTO.getTitle()));
        response.body("data.description", is(taskCreateDTO.getDescription()));
        response.body("data.createAt", notNullValue());
        response.body("data.complete", is(false));
    }

    @ParameterizedTest
    @MethodSource("invalidTaskProvider")
    public void testCreateTaskWithInvalidData(TaskCreateDTO taskCreateDTO, int expectedStatusCode) {
        String jwtAuth = JwtAuthTest.loginAndGetJwt();
        ValidatableResponse response =
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + jwtAuth)
                        .body(taskCreateDTO)
                        .when()
                        .post("/tasks")
                        .then()
                        .statusCode(expectedStatusCode);

        response.body("status", is(expectedStatusCode));
        response.body("title", notNullValue());
        response.body("violations", notNullValue());
    }

    @ParameterizedTest
    @MethodSource("taskUpdateDataProvider")
    void testUpdateTask(TaskUpdateDTO taskUpdateDTO, int expectedStatusCode) {
        String jwtAuth = JwtAuthTest.loginAndGetJwt();
        ValidatableResponse response =
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + jwtAuth)
                        .body(taskUpdateDTO)
                        .when()
                        .put("/tasks/1")  // Assumendo che l'ID del task sia 1
                        .then()
                        .statusCode(expectedStatusCode);

        if (expectedStatusCode != 200) {
            response.body("status", is(expectedStatusCode));
            response.body("title", notNullValue());
            response.body("violations", notNullValue());
        } else {

            response.body("data.id", is(1));
            response.body("data.title", notNullValue());
            response.body("data.description", notNullValue());
            response.body("data.complete",notNullValue());
        }
    }

    @ParameterizedTest
    @MethodSource("taskCreateDataProvider")
    void testDeleteTask(TaskCreateDTO taskCreateDTO) {
        String jwtAuth = JwtAuthTest.loginAndGetJwt();

        ValidatableResponse createResponse =
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + jwtAuth)
                        .body(taskCreateDTO)
                        .when()
                        .post("/tasks")
                        .then()
                        .statusCode(200);

        int createdTaskId = createResponse.extract().path("data.id");

            ValidatableResponse deleteResponse =
                    given()
                            .header("Authorization", "Bearer " + jwtAuth)
                            .when()
                            .delete("/tasks/" + createdTaskId)
                            .then()
                            .statusCode(200); // Status code per eliminazione riuscita

            deleteResponse.log().all();

    }

    // Metodo per fornire i dati per i test di creazione del task
    static Stream<TaskCreateDTO> taskCreateDataProvider() {
        long currentTimeMillis = System.currentTimeMillis();
        return Stream.of(
                new TaskCreateDTO("Titolo Task 1"+currentTimeMillis, "Descrizione Task 1"),
                new TaskCreateDTO("Titolo Task 2"+currentTimeMillis, "Descrizione Task 2")
        );
    }

    private static Stream<Arguments> invalidTaskProvider() {
        return Stream.of(
                // Titolo troppo lungo
                arguments(new TaskCreateDTO("This title is far too long to be valid because it exceeds 40 characters.", "Valid description"), 400),
                // Titolo vuoto
                arguments(new TaskCreateDTO("", "Valid description"), 400),
                // Descrizione vuota
                arguments(new TaskCreateDTO("Valid Title", ""), 400),
                // Titolo nullo
                arguments(new TaskCreateDTO(null, "Valid description"), 400),
                // Descrizione nulla
                arguments(new TaskCreateDTO("Valid Title", null), 400),
                //caso in cui title gia presente nel db!
                arguments(new TaskCreateDTO("Ciaoooo", "Another description"), 409)

        );
    }

    private static Stream<Arguments> taskUpdateDataProvider() {
        return Stream.of(
                // Casi validi
                Arguments.of(new TaskUpdateDTO("New Title", "Updated Description", true), 200),
                Arguments.of(new TaskUpdateDTO("New Title", null, false), 200),
                Arguments.of(new TaskUpdateDTO(null, "Updated Description", true), 200),
                Arguments.of(new TaskUpdateDTO("New Title", "Updated Description", null), 200),
                Arguments.of(new TaskUpdateDTO("Title", "Description", false), 200),

                // Casi non validi
                Arguments.of(new TaskUpdateDTO("", "Updated Description", true), 400),  // Titolo vuoto
                Arguments.of(new TaskUpdateDTO("Title that is far too long and exceeds the maximum character limit", "Description", true), 400),  // Titolo troppo lungo
                Arguments.of(new TaskUpdateDTO("Valid Title", "", true), 400),  // Descrizione vuota
                Arguments.of(new TaskUpdateDTO(null, "Valid description", true), 200),  // Titolo nullo
                Arguments.of(new TaskUpdateDTO("Valid Title5", null, true), 200),  // Descrizione nulla

                // Caso in cui il titolo è già presente nel DB
                Arguments.of(new TaskUpdateDTO("Ciaoooo", "Another description", true), 500)  // Titolo già presente
        );
    }
}
