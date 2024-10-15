package org.acme.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AuthenticationResponseDTO {

    private String accessToken;
    private String refreshToken;

    public static AuthenticationResponseDTO of (String access, String refresh){
        AuthenticationResponseDTO dto = new AuthenticationResponseDTO();
        dto.setAccessToken(access);
        dto.setRefreshToken(refresh);
        return dto;
    }
}
