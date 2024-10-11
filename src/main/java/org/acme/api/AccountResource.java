package org.acme.api;


import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.acme.dto.BaseSearchRequest;
import org.acme.dto.PageResult;
import org.acme.dto.account.*;
import org.acme.service.AccountService;
import org.acme.utils.response.SuccessResponse;
import org.acme.validation.ValidId;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)

public class AccountResource {

    @Inject
    AccountService accountService;

    @GET
    @RolesAllowed("Admin")
    public SuccessResponse<PageResult<AccountAdminResponseDTO>> findAccounts (
            @BeanParam @Valid BaseSearchRequest request
            ){
        return new SuccessResponse<>(accountService.findAccounts(request,null));
    }

    @GET
    @Path("/active")
    @RolesAllowed("Admin")
    public SuccessResponse<PageResult<AccountAdminResponseDTO>> findAccountsActive(
            @BeanParam @Valid BaseSearchRequest request
    ){
        return new SuccessResponse<>(accountService.findAccounts(request,false));
    }

    @GET
    @Path("/inactive")
    @RolesAllowed("Admin")
    public SuccessResponse<PageResult<AccountAdminResponseDTO>> findAccountsInactive(
            @BeanParam @Valid BaseSearchRequest request
    ){
        return new SuccessResponse<>(accountService.findAccounts(request,true));
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("Admin")
    public SuccessResponse<AccountResponseDTO> getAccountById(
            @PathParam("id") @ValidId long id
    ){
        return new SuccessResponse<>(accountService.getAccountById(id));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public SuccessResponse<AccountResponseDTO> createAccount(
            @Valid AccountCreateDTO createDTO
            ){
        return new SuccessResponse<>(accountService.createAccount(createDTO));
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    public SuccessResponse<AccountResponseDTO> updateAccount(
            @Valid AccountUpdateDTO updateDTO
            ){
        return new SuccessResponse<>(accountService.updateAccount(updateDTO));
    }

    @PUT
    @Path("/update-password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    public SuccessResponse<AccountResponseDTO> updateAccountPassword(
            @Valid AccountPasswordUpdateDTO updateDTO
            ){
        return new SuccessResponse<>(accountService.updateAccountPassword(updateDTO));
    }

    @DELETE
    @Authenticated
    public SuccessResponse<AccountResponseDTO> deleteAccount(){
        return new SuccessResponse<>(accountService.deleteAccount());
    }

}
