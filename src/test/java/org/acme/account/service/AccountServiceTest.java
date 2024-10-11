/*
package org.acme.account.service;


import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.dto.account.AccountCreateDTO;
import org.acme.dto.account.AccountPasswordUpdateDTO;
import org.acme.dto.account.AccountUpdateDTO;
import org.acme.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class AccountServiceTest {
*/

/*
    @Inject
    AccountRepository accountRepository;

    @Inject
    AccountMapper accountMapper;


    @Test
    public void validCreateAccountTest() {
        long currentTimeMillis = System.currentTimeMillis();

        AccountCreateDTO accountCreateDTO = new AccountCreateDTO();
        accountCreateDTO.setUsername("validUser"+currentTimeMillis);
        accountCreateDTO.setPassword("ValidPassword123");
        accountCreateDTO.setEmail("valid"+currentTimeMillis+"@example.com");

        ValidatableResponse response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(accountCreateDTO)
                .when()
                .post("/users/create-account")
                .then()
                .statusCode(200); // Aspettiamo un codice di stato 200


        response.body("status", is(200));
        response.body("data.username", is("ValidUser"+currentTimeMillis));
        response.body("data.email", is("valid"+currentTimeMillis+"@example.com"));
        response.body("data.createAt", notNullValue());
        response.body("data.updateAt", notNullValue());
        response.body("data.isDeleted", is(false));
        response.body("data.deleteAt", nullValue());
    }



    @ParameterizedTest
    @DisplayName("{0}")
    @MethodSource("invalidAccountCreateData")
    @Transactional
    public void invalidCreateAccountTest(TestCase testCase){

        given()
                    .contentType(ContentType.JSON)
                    .body(testCase.dto)
        .when()
            .post("/users/create-account")
        .then()
            .statusCode(400)
            .body("status", is(400))
            .body("message",is("Validation not work"))
            .body("details", notNullValue());

    }

    @Test
    @Transactional
    public void validUpdateUsernameTest() {
        long userId = 1; // ID dell'utente da aggiornare
        AccountUpdateDTO accountUpdateDTO = new AccountUpdateDTO();
        String newUsername = "ValidUserNew"; // Nuovo username valido

        accountUpdateDTO.setUsername(newUsername);

        // Esegui la richiesta PUT
        given()
                .pathParam("id", userId)
                .contentType(ContentType.JSON)
                .body(accountUpdateDTO)
                .when()
                .put("/users/{id}/update")
                .then()
                .statusCode(200) // Assicurati che lo stato sia 200 OK
                .body("status", is(200))
                .body("data.username", is(newUsername)); // Verifica che l'username sia aggiornato
    }

    @ParameterizedTest
    @MethodSource("invalidUsernameTestCases")
    @Transactional
    public void updateUsernameWithStreamTest(TestCase<AccountUpdateDTO> testCase) {
        long userId = 1; // ID dell'utente da aggiornare

        // Esegui la richiesta PUT e verifica il comportamento
        given()
                .pathParam("id", userId)
                .contentType(ContentType.JSON)
                .body(testCase.dto)
                .when()
                .put("/users/{id}/update")
                .then()
                .statusCode(400) // Assicurati che lo stato sia 400 Bad Request
                .body("status", is(400))
                .body("message", is("Validation not work"))
                .body("details", notNullValue()); // Verifica il messaggio di errore
    }

    @ParameterizedTest
    @MethodSource("invalidPasswordTestCases")
    @Transactional
    public void updatePasswordWithStreamTest(TestCase<AccountPasswordUpdateDTO> testCase) {
        long userId = 1; // ID dell'utente da aggiornare
        AccountPasswordUpdateDTO accountPasswordUpdateDTO = testCase.dto;

        // Esegui la richiesta PUT
        given()
                .pathParam("id", userId)
                .contentType(ContentType.JSON)
                .body(accountPasswordUpdateDTO)
                .when()
                .put("/users/{id}/update-password")
                .then()
                .statusCode(400)
                .body("status", is(400))
                .body("message", is("Validation not work"))
                .body("details", notNullValue());
    }

    @Test
    @Transactional
    public void updatePasswordSuccessTest() {
        long userId = createTestUser();
        AccountPasswordUpdateDTO accountPasswordUpdateDTO = new AccountPasswordUpdateDTO();
        accountPasswordUpdateDTO.setPassword("InitialPassword");
        accountPasswordUpdateDTO.setNewPassword("NewPassword");


        given()
                .pathParam("id", userId)
                .contentType(ContentType.JSON)
                .body(accountPasswordUpdateDTO)
                .when()
                .put("/users/{id}/update-password")
                .then()
                .statusCode(200)
                .log().all()
                .body("status", is(200))
                .body("data.username", notNullValue())
                .body("data.email", notNullValue())
                .body("data.createAt", notNullValue())
                .body("data.updateAt", notNullValue())
                .body("data.isDeleted", is(false))
                .body("data.deleteAt", nullValue());
    }

    @Test
    @Transactional
    public void deleteUserSuccessTest() {

        long userId = createTestUser();

        given()
                .pathParam("id", userId)
                .when()
                .delete("/users/{id}/remove")
                .then()
                //.log().all()
                .statusCode(200)
                .body("status", is(200))
                .body("data.username", notNullValue())
                .body("data.email", notNullValue())
                .body("data.createAt", notNullValue())
                .body("data.updateAt", notNullValue())
                .body("data.isDeleted",is(true))
                .body("data.deleteAt",notNullValue());

    }


    private static Stream<TestCase<AccountCreateDTO>> invalidAccountCreateData() {
        AccountCreateDTO nullUsername = new AccountCreateDTO();
        nullUsername.setUsername(null);
        nullUsername.setPassword("validPassword");
        nullUsername.setEmail("valid.email@example.com");

        AccountCreateDTO emptyUsername = new AccountCreateDTO();
        emptyUsername.setUsername("");
        emptyUsername.setPassword("validPassword");
        emptyUsername.setEmail("valid.email@example.com");

        AccountCreateDTO nullPassword = new AccountCreateDTO();
        nullPassword.setUsername("validUsername");
        nullPassword.setPassword(null);
        nullPassword.setEmail("valid.email@example.com");

        AccountCreateDTO emptyPassword = new AccountCreateDTO();
        emptyPassword.setUsername("validUsername");
        emptyPassword.setPassword("");
        emptyPassword.setEmail("valid.email@example.com");

        AccountCreateDTO invalidEmail = new AccountCreateDTO();
        invalidEmail.setUsername("validUsername");
        invalidEmail.setPassword("validPassword");
        invalidEmail.setEmail("invalidEmail");

        AccountCreateDTO longUsername = new AccountCreateDTO();
        longUsername.setUsername("thisIsAVeryLongUsernameThatExceedsLimit");
        longUsername.setPassword("validPassword");
        longUsername.setEmail("valid.email@example.com");

        AccountCreateDTO longEmail = new AccountCreateDTO();
        longEmail.setUsername("validUsername");
        longEmail.setPassword("validPassword");
        longEmail.setEmail("thisIsAVeryLongEmailThatExceedsTheLimit@exsdaasdasdasdadasasdasdadadample.com");

        return Stream.of(
                new TestCase<AccountCreateDTO>(nullUsername, "username cannot be null!"),
                new TestCase<AccountCreateDTO>(emptyUsername, "username cannot be empty"),
                new TestCase<AccountCreateDTO>(nullPassword, "password cannot be null!"),
                new TestCase<AccountCreateDTO>(emptyPassword, "password cannot be empty"),
                new TestCase<AccountCreateDTO>(invalidEmail, "invalid email"),
                new TestCase<AccountCreateDTO>(longUsername, "username must be between 1 and 30 characters"),
                new TestCase<AccountCreateDTO>(longEmail, "email must be between 8 and 16 characters")
        );
    }

    private static Stream<TestCase<AccountUpdateDTO>> invalidUsernameTestCases() {
        AccountUpdateDTO case1 = new AccountUpdateDTO();
        case1.setUsername(null); // Username nullo

        AccountUpdateDTO case2 = new AccountUpdateDTO();
        case2.setUsername(""); // Username vuoto

        AccountUpdateDTO case3 = new AccountUpdateDTO();
        case3.setUsername("a".repeat(31)); // Username troppo lungo

        return Stream.of(
                new TestCase<AccountUpdateDTO>(case1,"username cannot be null!"),
                new TestCase<AccountUpdateDTO>(case2,"username cannot be empty"),
                new TestCase<AccountUpdateDTO>(case3, "username must be between 1 and 30 characters")
                );
    }

    private static Stream<TestCase<AccountPasswordUpdateDTO>> invalidPasswordTestCases() {
        AccountPasswordUpdateDTO case1 = new AccountPasswordUpdateDTO();
        case1.setPassword(null); // Password attuale nulla

        AccountPasswordUpdateDTO case2 = new AccountPasswordUpdateDTO();
        case2.setPassword(""); // Password attuale vuota

        AccountPasswordUpdateDTO case3 = new AccountPasswordUpdateDTO();
        case3.setPassword("short"); // Password attuale troppo corta

        AccountPasswordUpdateDTO case4 = new AccountPasswordUpdateDTO();
        case4.setPassword("a".repeat(17)); // Password attuale troppo lunga

        AccountPasswordUpdateDTO case5 = new AccountPasswordUpdateDTO();
        case5.setNewPassword(null); // Nuova password nulla

        AccountPasswordUpdateDTO case6 = new AccountPasswordUpdateDTO();
        case6.setNewPassword(""); // Nuova password vuota

        AccountPasswordUpdateDTO case7 = new AccountPasswordUpdateDTO();
        case7.setNewPassword("short"); // Nuova password troppo corta

        AccountPasswordUpdateDTO case8 = new AccountPasswordUpdateDTO();
        case8.setNewPassword("a".repeat(17)); // Nuova password troppo lunga

        return Stream.of(
                new TestCase<>(case1, "password cannot be null!"),
                new TestCase<>(case2, "password cannot be empty"),
                new TestCase<>(case3, "password must be between 8 and 16 characters"),
                new TestCase<>(case4, "password must be between 8 and 16 characters"),
                new TestCase<>(case5, "new password cannot be null!"),
                new TestCase<>(case6, "new password cannot be empty"),
                new TestCase<>(case7, "new password must be between 8 and 16 characters"),
                new TestCase<>(case8, "new password must be between 8 and 16 characters")
        );
    }

    private long createTestUser() {
        AccountCreateDTO accountCreateDTO = new AccountCreateDTO();
        accountCreateDTO.setUsername("testUser" + System.currentTimeMillis()); // Username univoco
        accountCreateDTO.setPassword("InitialPassword");
        accountCreateDTO.setEmail("testUser" + System.currentTimeMillis() + "@example.com");

        int userId = given()
                .contentType(ContentType.JSON)
                .body(accountCreateDTO)
                .when()
                .post("/users/create-account")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .path("data.id");

        return  userId;
    }

    static class TestCase<T> {
        T dto;
        String expectedMessage;

        TestCase(T dto, String expectedMessage) {
            this.dto = dto;
            this.expectedMessage = expectedMessage;
        }
    }
*/



