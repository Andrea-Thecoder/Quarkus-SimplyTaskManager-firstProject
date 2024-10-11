package org.acme.service.impl;

import org.acme.dto.BaseSearchRequest;
import org.acme.dto.PageResult;
import org.acme.dto.role.AccountRoleDTO;
import org.acme.dto.role.AccountRoleResponseDTO;

public interface IAccountRoleService {

    PageResult<AccountRoleResponseDTO> findRoles(BaseSearchRequest request);
    AccountRoleResponseDTO getRoleById(long id );
    AccountRoleResponseDTO createRole (AccountRoleDTO roleDTO);
    AccountRoleResponseDTO updateRole (long id,AccountRoleDTO roleDTO);
    AccountRoleResponseDTO deleteRole (long id);

}
