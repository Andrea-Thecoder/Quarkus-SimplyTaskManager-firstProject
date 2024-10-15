package org.acme.util;

import io.restassured.http.ContentType;
import org.acme.dto.auth.AuthenticationLoginDTO;

import static io.restassured.RestAssured.given;
public class JwtAuthTest {

    public static String loginAndGetJwt() {
        // Crea le credenziali per il login
        AuthenticationLoginDTO login = new AuthenticationLoginDTO();
        login.setEmail("goldtrue2@io.it");
        login.setPassword("12345678");

        String jwt = given()
                .contentType(ContentType.JSON)
                .body(login)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .path("data.accessToken");

        System.out.println("JWT Token: " + jwt); // Aggiungi questa riga
        return jwt;
    }

    public static String loginAndGetJwt(String email, String password) {
        // Crea le credenziali per il login
        AuthenticationLoginDTO login = new AuthenticationLoginDTO();
        login.setEmail(email);
        login.setPassword(password);

        String jwt = given()
                .contentType(ContentType.JSON)
                .body(login)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .path("data.accessToken");

        System.out.println("JWT Token: " + jwt); // Aggiungi questa riga
        return jwt;
    }
}
