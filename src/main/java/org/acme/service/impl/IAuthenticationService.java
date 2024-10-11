package org.acme.service.impl;

import org.acme.dto.AuthenticationLoginDTO;

public interface IAuthenticationService {

    String login(AuthenticationLoginDTO loginDTO);
}
