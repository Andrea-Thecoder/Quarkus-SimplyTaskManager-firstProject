package org.acme.service;


import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import org.acme.dto.AuthenticationLoginDTO;
import org.acme.model.Account;
import org.acme.repository.AccountRepository;
import org.acme.repository.AuthenticationRepository;
import org.acme.security.PasswordUtils;
import org.acme.utils.exception.InvalidPasswordException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.time.Instant;

@ApplicationScoped
public class AuthenticationService {

    @Inject
    AuthenticationRepository authRepository;

    @Inject
    AccountRepository accountRepository;

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String jwtIssuer;

    private static final Logger LOG = Logger.getLogger(AuthenticationService.class);

    public String login (AuthenticationLoginDTO loginDTO){

        String email = loginDTO.getEmail();
        Account account = accountRepository.find("email = ?1 AND isDeleted = false",email)                                                                           .firstResultOptional()
                                                                        .orElseThrow(
                                                                                () -> new EntityNotFoundException("Account with email: "+email+" does not exist!"));

    String hashedPassword = account.getPassword();
    String password  = loginDTO.getPassword();

    if(!PasswordUtils.checkPassword(password,hashedPassword)) throw new InvalidPasswordException("Current passwords not match!");

    long userId = account.getId();
    String roleName= account.getRole().getRoleName();


    return Jwt.issuer(jwtIssuer)   //firma chi è il mittente del token
            .upn(email)   //UserPrincipalName viene utilizzato per identificare in modo unifoco l'utente e la sua autorizzazione
            //.groups(roles)  //qui si inserisono i gruppi dell'utente ovvero che ruoli appartiene .
            .subject(String.valueOf(userId))
            // Claim personalizzato , informazion iaggiuntive che contiene il token utile perutilizzi psot auth.
            .claim("email", email)
            .groups(roleName)
            .expiresAt(Instant.now().plusSeconds(3600)) //ogni JWT deve avere una scadenza, essa è espressa in data ed ora, nel modo che abbiamo approcciato stiam odicendoche dura 1 ora.
            .sign(); //firma il token e quindi crea la stringa codificata.
    }
}
