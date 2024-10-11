package org.acme.security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    //numero di round che fa il salt, aumenta la randomicità dell'hash, aumentando cosi la sicurezza. Il costo è nella performance, più questo valore è alto maggiore tempo e performance richiederà .
    private static final int COST_FACTOR = 10;


    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(COST_FACTOR));
    }

    public static boolean checkPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}
