package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.acme.dto.account.AccountCreateDTO;
import org.acme.dto.auth.AuthenticationLoginDTO;
import org.acme.dto.profile.ProfileCreateDTO;
import org.acme.util.JwtAuthTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;



@QuarkusTest
public class AuthenticationServiceTest {

    @Test
    public void TestLoginFailed(){

    AccountCreateDTO account = createAccount();

                given()
                        .contentType(ContentType.JSON)
                        .body(account)
                        .when()
                        .post("/accounts")
                        .then()
                        .log().all()
                        .statusCode(200);

        String jwtToken = JwtAuthTest.loginAndGetJwt(
                account.getEmail(),
                account.getPassword()
        );

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .delete("/accounts")
                .then()
                .log().all()
                .statusCode(200);

        AuthenticationLoginDTO login =  new AuthenticationLoginDTO();
        login.setEmail(account.getEmail());
        login.setPassword(account.getPassword());

        ValidatableResponse response = given()
                .contentType(ContentType.JSON)
                .body(login)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(409)
                .log().all();
        response.body("status", is(409));
        response.body("title", notNullValue());
        response.body("violations",notNullValue());
    }



    private AccountCreateDTO createAccount(){
        long currentTimeMillis = System.currentTimeMillis();
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
        accountCreateDTO.setRoleName("Admin");
        accountCreateDTO.setProfile(profile);
        return accountCreateDTO;
    }
}
