package org.acme.api;


import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.acme.dto.BaseSearchRequest;
import org.acme.dto.PageResult;
import org.acme.dto.response.SuccessResponse;
import org.acme.dto.role.AccountRoleDTO;
import org.acme.dto.role.AccountRoleResponseDTO;
import org.acme.service.AccountRoleService;
import org.acme.validation.ValidId;


@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("Admin")
public class AccountRoleResource {

    @Inject
    AccountRoleService roleService;

    @GET
    public SuccessResponse<PageResult<AccountRoleResponseDTO>> findRoles(
            @BeanParam @Valid BaseSearchRequest request
            ){
        return new SuccessResponse<>(roleService.findRoles(request));
    }

    @GET
    @Path("/{id}")
    public SuccessResponse<AccountRoleResponseDTO> getRoleById(
            @PathParam("id") @ValidId long id
    ){
      return new SuccessResponse<>(roleService.getRoleById(id));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public SuccessResponse<AccountRoleResponseDTO> createRole(
            @Valid AccountRoleDTO roleDTO
    ){
        return new SuccessResponse<>(roleService.createRole(roleDTO));
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public SuccessResponse<AccountRoleResponseDTO> updateRole(
            @PathParam("id") @ValidId long id,
            @Valid AccountRoleDTO roleDTO
    ){
        return new SuccessResponse<>(roleService.updateRole(id,roleDTO));
    }

    @DELETE
   @Path("/{id}")
    public SuccessResponse<AccountRoleResponseDTO> deleteRole(
            @PathParam("id") @ValidId long id
    ){
        return new SuccessResponse<>(roleService.deleteRole(id));
    }

}
