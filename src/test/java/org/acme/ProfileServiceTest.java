package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.acme.dto.profile.ProfileUpdateDTO;
import org.acme.util.JwtAuthTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class ProfileServiceTest {

    @Test
    void testGetProfile() {
        // Autenticati e ottieni un JWT
        String jwtAuth = JwtAuthTest.loginAndGetJwt();  // Assicurati che questo metodo funzioni correttamente

        // Effettua la richiesta GET al profilo
        ValidatableResponse response =
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + jwtAuth) // Aggiungi il token JWT
                        .when()
                        .get("/profiles") // Endpoint per ottenere il profilo
                        .then()
                        .statusCode(200); // Verifica che il codice di stato sia 200 OK

        // Verifica il contenuto della risposta
        response.body("data.firstname", notNullValue())
                .body("data.lastname", notNullValue())
                .body("data.country", notNullValue())
                .body("data.phoneNumber", notNullValue())
                .body("data.taxCode", notNullValue())
                .body("data.birthDate", notNullValue())
                .body("data.updateAt", notNullValue());
    }

    @ParameterizedTest
    @MethodSource("profileUpdateDataProvider")
    void testUpdateProfile(ProfileUpdateDTO profileUpdateDTO, int expectedStatus) {

        String jwtAuth = JwtAuthTest.loginAndGetJwt();


        ValidatableResponse response = given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + jwtAuth)
                        .body(profileUpdateDTO)
                        .when()
                        .put("/profiles")
                        .then()
                        .log().all()
                        .statusCode(expectedStatus);


        if (expectedStatus == 200) {
            response.body("data.firstname", is(profileUpdateDTO.getFirstname()))
                    .body("data.lastname", is(profileUpdateDTO.getLastname()))
                    .body("data.country", is(profileUpdateDTO.getCountry()))
                    .body("data.phoneNumber", is(profileUpdateDTO.getPhoneNumber()))
                    .body("data.taxCode", is(profileUpdateDTO.getTaxCode()))
                    .body("data.birthDate", is(profileUpdateDTO.getBirthDate().toString()));

        } else {

            response.body("title", notNullValue())
                    .body("violations", notNullValue())
                    .body("status", notNullValue());


        }
    }





    static Stream<Arguments> profileUpdateDataProvider() {
        return Stream.of(
                // Casi di successo
                Arguments.of(new ProfileUpdateDTO("John", "Doe", "Italy", "+12345678900", "ABC1234567890123", LocalDate.of(1990, 1, 1)), 200),
                Arguments.of(new ProfileUpdateDTO("Jane", "Smith", "Usa", "+198765432100", "DEF1234567890123", LocalDate.of(1985, 5, 15)), 200),

                // Casi di fallimento
                Arguments.of(new ProfileUpdateDTO("", "Doe", "Italy", "+1234567890", "ABC1234567890123", LocalDate.of(1990, 1, 1)), 400), // firstname vuoto
                Arguments.of(new ProfileUpdateDTO("John", "", "Italy", "+1234567890", "ABC1234567890123", LocalDate.of(1990, 1, 1)), 400), // lastname vuoto
                Arguments.of(new ProfileUpdateDTO("John", "Doe", "", "+1234567890", "ABC1234567890123", LocalDate.of(1990, 1, 1)), 400), // country vuoto
                Arguments.of(new ProfileUpdateDTO("John", "Doe", "Italy", "12345", "ABC1234567890123", LocalDate.of(1990, 1, 1)), 400), // phone number non valido
                Arguments.of(new ProfileUpdateDTO("John", "Doe", "Italy", "+1234567890", "ABC", LocalDate.of(1990, 1, 1)), 400), // taxCode troppo corto
                Arguments.of(new ProfileUpdateDTO("John", "Doe", "Italy", "+1234567890", "ABC12345678901234", LocalDate.of(1990, 1, 1)), 400) // taxCode troppo lungo
        );
    }
}
