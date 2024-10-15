package org.acme.service;


import io.quarkus.security.UnauthorizedException;
import io.smallrye.jwt.auth.principal.JWTCallerPrincipal;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import org.acme.dto.auth.AuthenticationLoginDTO;
import org.acme.dto.auth.AuthenticationResponseDTO;
import org.acme.dto.auth.AuthenticationSendTokenDTO;
import org.acme.model.Account;
import org.acme.repository.AccountRepository;
import org.acme.repository.AuthenticationRepository;
import org.acme.security.PasswordUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
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

    @Inject
    JsonWebToken jwt;

    @Inject
    JWTParser jwtParser;

    private static final Logger LOG = Logger.getLogger(AuthenticationService.class);

    public AuthenticationResponseDTO login (AuthenticationLoginDTO loginDTO){

        String email = loginDTO.getEmail();
        Account account  = getAccountOrThrow(email);
        String hashedPassword = account.getPassword();
        String password  = loginDTO.getPassword();

        if(!PasswordUtils.checkPassword(password,hashedPassword)) throw new UnauthorizedException("Email or password not match");

        long userId = account.getId();
        String roleName= account.getRole().getRoleName();


        String accessToken = generateAccessToken(userId,email,roleName);
        String refreshToken = generateRefreshToken(userId);

        return AuthenticationResponseDTO.of(accessToken,refreshToken);
    }

    public String regenerateAccessToken(AuthenticationSendTokenDTO tokenDTO){

        try {
            JWTCallerPrincipal principal = (JWTCallerPrincipal) jwtParser.parse(tokenDTO.getRefreshToken());
            if (principal.getExpirationTime() > Instant.now().toEpochMilli()) {
                throw new UnauthorizedException("Refresh token is expired!");
            }
            long userId = Long.parseLong(principal.getSubject());
            Account account = getAccountOrThrow(userId);
            String roleName= account.getRole().getRoleName();
            String email = account.getEmail();
            return generateAccessToken(userId,email,roleName);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateAccessToken(long userId,String email, String roleName){
        return Jwt.issuer(jwtIssuer)   //firma chi è il mittente del token
                .upn(email)   //UserPrincipalName viene utilizzato per identificare in modo unifoco l'utente e la sua autorizzazione
                //.groups(roles)  //qui si inserisono i gruppi dell'utente ovvero che ruoli appartiene .
                .subject(String.valueOf(userId))
                // Claim personalizzato , informazion iaggiuntive che contiene il token utile perutilizzi psot auth.
                .claim("email", email)
                .groups(roleName)
                .expiresAt(Instant.now().plusSeconds(900)) //ogni JWT deve avere una scadenza, essa è espressa in data ed ora, nel modo che abbiamo approcciato stiam odicendoche dura 1 ora.
                .sign(); //firma il token e quindi crea la stringa codificata.

    }

    private String generateRefreshToken(long userId){
        return Jwt.issuer(jwtIssuer)
                .subject(String.valueOf(userId))
                .expiresAt(Instant.now().plusSeconds(86400))
                .sign();
    }

    private Account getAccountOrThrow(long id){
        return accountRepository.find("id = ?1 AND isDeleted = false",id)                                                                           .firstResultOptional()
                .orElseThrow(
                        () -> new EntityNotFoundException("Account with id: "+id+" does not exist!"));
    }
    private Account getAccountOrThrow(String email){
        return accountRepository.find("email = ?1 AND isDeleted = false",email)                                                                           .firstResultOptional()
                .orElseThrow(
                        () -> new EntityNotFoundException("Account with email: "+email+" does not exist!"));
    }


}
