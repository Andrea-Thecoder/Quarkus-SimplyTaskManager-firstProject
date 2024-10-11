package org.acme.service;


import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.acme.dto.BaseSearchRequest;
import org.acme.dto.PageResult;
import org.acme.dto.account.*;
import org.acme.model.Account;
import org.acme.repository.AccountRepository;
import org.acme.security.PasswordUtils;
import org.acme.utils.exception.InvalidPasswordException;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class AccountService {

    @Inject
    AccountRepository accountRepository;

    @Inject
    AccountRoleService roleService;

    @Inject
    JsonWebToken jwt;

    public PageResult<AccountAdminResponseDTO> findAccounts(BaseSearchRequest request, Boolean deleteFilter){
        String queryString ="";
        if(deleteFilter != null){
            queryString += String.format("isDeleted = %b ",deleteFilter);
        }
        if(StringUtils.isNotBlank(request.getSort())){
            queryString += String.format(" order by %s",request.getSort());
        } else{
            queryString += " order by id";
        }
        PanacheQuery<Account> query = accountRepository.find(queryString).page(request.toPage());
        List<Account> accounts = query.list();
        Page page = query.page();
        List<AccountAdminResponseDTO> accountDto = accounts.stream().map(AccountAdminResponseDTO::of).toList();
        return  PageResult.of(accountDto,page.index,page.size,query.count());
    }


    public AccountResponseDTO getAccountById(long id){
        return AccountResponseDTO.of(getAccountOrThrow(id));
    }


    @Transactional
    public AccountResponseDTO createAccount (AccountCreateDTO createDTO){

        CheckEmail(createDTO.getEmail());
        String hashedPassword= PasswordUtils.hashPassword(createDTO.getPassword());
        createDTO.setPassword(hashedPassword);
        Account newAccount  = createDTO.toEntity();

        createDTO.setRoleName(
                    StringUtils.defaultIfBlank(createDTO.getRoleName(), "User") //controlla se il primo parametro è NON nulla NON vuoto ,se è cosi restiuisce il primo parametro, altrimenti il secondo.
                    );
        newAccount.setRole(roleService.getRoleOrThrow(createDTO.getRoleName()));

        accountRepository.persist(newAccount);

        return AccountResponseDTO.of(newAccount);





    }

    @Transactional
    public AccountResponseDTO updateAccount(AccountUpdateDTO updateDTO){
        long userId = Long.parseLong(jwt.getSubject());
        Account account = getAccountOrThrow(userId);
        account.setUsername(updateDTO.getUsername());
        accountRepository.persist(account);
        return AccountResponseDTO.of(account);
    }

    @Transactional
    public AccountResponseDTO updateAccountPassword(AccountPasswordUpdateDTO updateDTO){

        if(!updateDTO.getPassword().equals(updateDTO.getRepeatPassword()))
            throw new InvalidPasswordException("Password and repeatPassword do not match ");

        long userId = Long.parseLong(jwt.getSubject());
        Account account = getAccountOrThrow(userId);
        if(!PasswordUtils.checkPassword(updateDTO.getPassword(),account.getPassword()))
            throw new InvalidPasswordException("Password do not match ");

        account.setPassword(PasswordUtils.hashPassword(updateDTO.getNewPassword()));

        accountRepository.persist(account);

        return AccountResponseDTO.of(account);
    }

    @Transactional
    public AccountResponseDTO deleteAccount(){
        long userId = Long.parseLong(jwt.getSubject());
        Account account = getAccountOrThrow(userId);
        account.setDeleted(true);
        account.setDeleteAt(LocalDateTime.now());
        account.setRole(roleService.getRoleOrThrow("Inactive"));
        accountRepository.persist(account);
        return AccountResponseDTO.of(account);
    }


    public long countRoleByRoleId (long id){
        return accountRepository.count("role.id = ?1",id);
    }

    public Account getAccountOrThrow(long id){
        return accountRepository.find("id = ?1", id).firstResultOptional().orElseThrow(
                ()-> new EntityNotFoundException("Account with id: "+ id + " does not Exist")
        );
    }

    private void CheckEmail(String email){
        accountRepository.find("email = ?1",email).firstResultOptional().ifPresent(
                account -> {
                    throw new EntityExistsException("Account with email: " + email + " alredy exist");
                });
    }
}
