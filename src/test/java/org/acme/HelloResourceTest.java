package org.acme;


import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;


@QuarkusTest //annotazioen basilare per far capire che è una classe di test per quarkus!
public class HelloResourceTest {

    @Test //annotazioen di junit per far capire che stiamo andando a creare un method per test
    public void testHello() //signature del test scritta in modo tradizionale
    {
        given() //given è  il method d'ingresso o meglio di preparazione per la request TEST  che stiamo andando a compialre, possiamo concatenare vari aspetti della request, ad esempio un header, un body etc.
                .when() // quando si esegue l'azione (l'azione viene specificata dal method concatenatosuccessivo e può essere get/post etc
                    .get("/hello") //l'azione può avere un url come parametro per simulare una request endpoint reale.
                .then() //zona delle asserzioni, qui inseriamo le nostre aspettative, ovvero quello che ci aspettiamo che accada quando si esegue l'azione.
                .statusCode(200) //ci aspettiamo uno status code 200, in quanto questo endpoint restituisce solo una stringa non può fallire!
                .body(is("Hello from Quarkus REST")); //controlla che il body di response sia esattamente (is) la stringa li indicata.
    }
}
