package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import jakarta.transaction.Transactional;
import org.acme.dto.account.AccountCreateDTO;
import org.acme.dto.account.AccountPasswordUpdateDTO;
import org.acme.dto.account.AccountUpdateDTO;
import org.acme.dto.profile.ProfileCreateDTO;
import org.acme.util.JwtAuthTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@QuarkusTest
public class AccountServiceTest{

    @Test
    @Transactional
    public void testCreateAccountSuccess() {
        long currentTimeMillis = System.currentTimeMillis();
        AccountCreateDTO accountCreateDTO = createAccount(currentTimeMillis);

        ValidatableResponse response =
                given()
                        .contentType(ContentType.JSON)
                        .body(accountCreateDTO)
                        .when()
                        .post("/accounts")
                        .then()
                        .statusCode(200);

        response.body("status", is(200));
        response.body("data.username", is("testuser"));
        response.body("data.email", is("test"+currentTimeMillis+"@example.com"));
        response.body("data.roleName", is("User"));
        response.body("data.id",is(not(0L)));
        response.body("data.tasks",is(empty()));
    }

    @Test
    @Transactional
    public void testCreateAccountFailure_EmailAlreadyExists() {
        long currentTimeMillis = System.currentTimeMillis();
        ProfileCreateDTO profile = new ProfileCreateDTO();
        profile.setFirstname("Jane");
        profile.setLastname("Doe");
        profile.setCountry("Italy");
        profile.setPhoneNumber("+393481234567");
        profile.setTaxCode("RSSMRA85M01H501Z");  // 16 caratteri come richiesto
        profile.setBirthDate(LocalDate.of(1985, 1, 1));

        AccountCreateDTO accountCreateDTO = new AccountCreateDTO();
        accountCreateDTO.setUsername("testuser");
        accountCreateDTO.setEmail("truegold@it.it");  // Email già esistente
        accountCreateDTO.setPassword("password123");
        accountCreateDTO.setProfile(profile);

        // Effettua la richiesta e verifica la risposta
        ValidatableResponse response =
                given()
                        .contentType(ContentType.JSON)
                        .body(accountCreateDTO)
                        .when()
                        .post("/accounts")
                        .then()
                        .statusCode(409);

        // Verifica che il messaggio di errore sia presente (assicurati che il tuo API restituisca un messaggio di errore)
        response.body("status", is(409));
        response.body("title", notNullValue());
        response.body("violations",notNullValue());
    }

    @Test
    @Transactional
    public void testUpdateAccountUsernameSuccess() {

        String jwtAuth = JwtAuthTest.loginAndGetJwt();
        System.out.println("er token"+jwtAuth);
        AccountUpdateDTO accountUpdateDTO = new AccountUpdateDTO();
        accountUpdateDTO.setUsername("newUsername");

        // Effettua la richiesta PUT e verifica la risposta
        ValidatableResponse response =
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + jwtAuth)
                        .body(accountUpdateDTO)
                        .when()
                        .put("/accounts") // Aggiorna l'endpoint in base alla tua API
                        .then()
                        .log().all()
                        .statusCode(200);

        // Verifica la risposta
        response.body("status", is(200));
        response.body("data.username", is("newUsername"));
        response.body("data.email", notNullValue());
        response.body("data.roleName", notNullValue());
        response.body("data.id",notNullValue());
        response.body("data.tasks",notNullValue());
    }

    @ParameterizedTest
    @MethodSource("invalidUsernameTestCases")
    @DisplayName("{0}")
    @Transactional
    public void testUpdateAccountUsernameFailure(AccountUpdateDTO updateDTO) {
        String jwtAuth = JwtAuthTest.loginAndGetJwt(); // Assicurati di avere un token valido
        System.out.println("JWT Token: " + jwtAuth);

        // Effettua la richiesta PUT e verifica la risposta
        ValidatableResponse response =
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + jwtAuth)
                        .body(updateDTO)
                        .when()
                        .put("/accounts") // Aggiorna l'endpoint in base alla tua API
                        .then()
                        .log().all()
                        .statusCode(400); // Aspettati un errore 400 Bad Request

        // Verifica la risposta
        response.body("status", is(400));
        response.body("title", notNullValue());
        response.body("violations",notNullValue());
    }


    @ParameterizedTest
    @MethodSource("org.acme.util.AccountPasswordTestData#provideInvalidPasswordUpdateDTOs")
    @Transactional
    public void testUpdatePasswordFailure(AccountPasswordUpdateDTO accountPasswordUpdateDTO) {
        String jwtAuth = JwtAuthTest.loginAndGetJwt();


        // Effettua la richiesta PUT e verifica la risposta
        ValidatableResponse response =
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + jwtAuth)
                        .body(accountPasswordUpdateDTO)
                        .when()
                        .put("/accounts/update-password") // Aggiorna l'endpoint in base alla tua API
                        .then()
                        .log().all()
                        .statusCode(400); // Aspettati un errore 400 Bad Request

        // Verifica la risposta
        response.body("status", is(400));
        response.body("title", notNullValue());
        response.body("violations",notNullValue());
    }

@Test
@Transactional
public void testUpdatePasswordAccountSuccess(){
        AccountPasswordUpdateDTO updateDTO = new AccountPasswordUpdateDTO();
        updateDTO.setPassword("12345678");
        updateDTO.setRepeatPassword("12345678");
    updateDTO.setNewPassword("12345678");

    String jwtAuth = JwtAuthTest.loginAndGetJwt();

    ValidatableResponse response =
            given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + jwtAuth)
                    .body(updateDTO)
                    .when()
                    .put("/accounts/update-password")
                    .then()
                    .log().all()
                    .statusCode(200);

    response.body("status", is(200));
    response.body("data.username", is("newUsername"));
    response.body("data.email", notNullValue());
    response.body("data.roleName", notNullValue());
    response.body("data.id",notNullValue());
    response.body("data.tasks",notNullValue());
    }

    @Test
    @Transactional
    public void testDeleteAccountSuccess(){
        long currentTimeMillis = System.currentTimeMillis();
        AccountCreateDTO accountCreateDTO = createAccount(currentTimeMillis);
                given()
                        .contentType(ContentType.JSON)
                        .body(accountCreateDTO)
                        .when()
                        .post("/accounts")
                        .then()
                        .statusCode(200);

        String jwtAuth  = JwtAuthTest.loginAndGetJwt(
                accountCreateDTO.getEmail(),
                accountCreateDTO.getPassword()
        );

        ValidatableResponse response =
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + jwtAuth)
                        .when()
                        .delete("/accounts")
                        .then()
                        .log().all()
                        .statusCode(200);

        response.body("status", is(200));
        response.body("data.username", is("testuser"));
        response.body("data.email", notNullValue());
        response.body("data.roleName", notNullValue());
        response.body("data.id",notNullValue());
        response.body("data.tasks",notNullValue());
    }




   private static Stream<AccountUpdateDTO> invalidUsernameTestCases() {
        AccountUpdateDTO case1 = new AccountUpdateDTO();
        case1.setUsername("                       ");

        AccountUpdateDTO case2 = new AccountUpdateDTO();
        case2.setUsername("");

        AccountUpdateDTO case3 = new AccountUpdateDTO();
        case3.setUsername("a".repeat(31));

        return Stream.of(
                case1,case2,case3
        );
        }



    private AccountCreateDTO createAccount(long currentTimeMillis){
        ProfileCreateDTO profile = new ProfileCreateDTO();
        profile.setFirstname("John");
        profile.setLastname("Doe");
        profile.setCountry("Italy");
        profile.setPhoneNumber("+393481234567");
        profile.setTaxCode("RSSMRA85M01H501Z");  // 16 caratteri come richiesto
        profile.setBirthDate(LocalDate.of(1985, 1, 1));

        AccountCreateDTO accountCreateDTO = new AccountCreateDTO();
        accountCreateDTO.setUsername("testuser");
        accountCreateDTO.setEmail("test"+currentTimeMillis+"@example.com");
        accountCreateDTO.setPassword("12345678");
        accountCreateDTO.setProfile(profile);
        return accountCreateDTO;
    }

}


