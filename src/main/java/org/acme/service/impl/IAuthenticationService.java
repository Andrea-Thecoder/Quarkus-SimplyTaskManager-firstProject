package org.acme.service.impl;

import org.acme.dto.auth.AuthenticationLoginDTO;

public interface IAuthenticationService {

    String login(AuthenticationLoginDTO loginDTO);
}
