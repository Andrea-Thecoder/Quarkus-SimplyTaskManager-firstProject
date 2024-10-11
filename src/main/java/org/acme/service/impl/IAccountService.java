package org.acme.service.impl;

import org.acme.dto.BaseSearchRequest;
import org.acme.dto.PageResult;
import org.acme.dto.account.*;

public interface IAccountService {

    PageResult<AccountAdminResponseDTO> findAccounts(BaseSearchRequest request, Boolean deleteFilter);
    AccountResponseDTO getAccountById(long id);
    AccountResponseDTO createAccount (AccountCreateDTO createDTO);
    AccountResponseDTO updateAccount(AccountUpdateDTO updateDTO);
    AccountResponseDTO updateAccountPassword(AccountPasswordUpdateDTO updateDTO);
    AccountResponseDTO deleteAccount();

}
