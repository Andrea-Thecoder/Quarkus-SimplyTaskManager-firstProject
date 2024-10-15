package org.acme.util;

import org.acme.dto.account.AccountPasswordUpdateDTO;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class AccountPasswordTestData {

    public static Stream<Arguments> provideInvalidPasswordUpdateDTOs() {
        return Stream.of(
                // password vuota
                Arguments.of(createPasswordUpdateDTO("", "12345678", "12345678")),
                // password solo spazi
                Arguments.of(createPasswordUpdateDTO("    ", "12345678", "newPass123")),
                // password troppo corta
                Arguments.of(createPasswordUpdateDTO("short", "12345678", "12345678")),
                // password troppo lunga
                Arguments.of(createPasswordUpdateDTO("thisPasswordIsWayTooLong123", "12345678", "12345678")),
                // repeatPassword vuota
                Arguments.of(createPasswordUpdateDTO("12345678", "", "newPass123")),
                // repeatPassword solo spazi
                Arguments.of(createPasswordUpdateDTO("12345678", "    ", "12345678")),
                // repeatPassword troppo corta
                Arguments.of(createPasswordUpdateDTO("12345678", "short", "12345678")),
                // repeatPassword troppo lunga
                Arguments.of(createPasswordUpdateDTO("12345678", "thisRepeatPasswordIsWayTooLong123", "12345678")),
                // newPassword vuota
                Arguments.of(createPasswordUpdateDTO("12345678", "12345678", "")),
                // newPassword solo spazi
                Arguments.of(createPasswordUpdateDTO("12345678", "12345678", "    ")),
                // newPassword troppo corta
                Arguments.of(createPasswordUpdateDTO("12345678", "12345678", "short")),
                // newPassword troppo lunga
                Arguments.of(createPasswordUpdateDTO("12345678", "12345678", "thisNewPasswordIsWayTooLong123"))
        );
    }

    private static AccountPasswordUpdateDTO createPasswordUpdateDTO(String password, String repeatPassword, String newPassword) {
        AccountPasswordUpdateDTO dto = new AccountPasswordUpdateDTO();
        dto.setPassword(password); // Usa il setter per applicare il trimming
        dto.setRepeatPassword(repeatPassword); // Usa il setter per applicare il trimming
        dto.setNewPassword(newPassword); // Usa il setter per applicare il trimming
        return dto;
    }
}

