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
import org.acme.dto.role.AccountRoleDTO;
import org.acme.dto.role.AccountRoleResponseDTO;
import org.acme.exception.RoleDeleteException;
import org.acme.model.AccountRole;
import org.acme.repository.AccountRoleRepository;
import org.acme.service.impl.IAccountRoleService;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@ApplicationScoped
public class AccountRoleService implements IAccountRoleService {

    @Inject
    AccountRoleRepository roleRepository;

    @Inject
    AccountService accountService;

    @Inject
    JsonWebToken jwt;

    public PageResult<AccountRoleResponseDTO> findRoles(BaseSearchRequest request){

        String queryString;
        if(StringUtils.isNotBlank(request.getSort())){
            queryString = String.format(" order by %s", request.getSort());
        } else {
            queryString = " order by id";
        }

        PanacheQuery<AccountRole> query = this.roleRepository.find(queryString);
        query.page(request.toPage());

        List<AccountRole> role = query.list();

        Page page = query.page();
        List<AccountRoleResponseDTO> dtoList = role.stream().map(AccountRoleResponseDTO::of).toList();
        return PageResult.of(dtoList, page.index, page.size, query.count());
    }

    public AccountRoleResponseDTO getRoleById(long id ){

        AccountRole role = getRoleOrThrow(id);

        return AccountRoleResponseDTO.of(role);
    }

    @Transactional
    public  AccountRoleResponseDTO createRole (AccountRoleDTO roleDTO){

        checkRole(roleDTO.getRoleName());

        AccountRole role = roleDTO.toEntity();

        roleRepository.persist(role);

        return AccountRoleResponseDTO.of(role);
    }

    @Transactional
    public AccountRoleResponseDTO updateRole (long id,AccountRoleDTO roleDTO){
        AccountRole role = getRoleOrThrow(id);
        role.setRoleName(roleDTO.getRoleName());
        roleRepository.persist(role);
        return AccountRoleResponseDTO.of(role);
    }

    @Transactional
    public AccountRoleResponseDTO deleteRole (long id){
        checkCount(id);
        AccountRole role = getRoleOrThrow(id);
        roleRepository.delete(role);
        return AccountRoleResponseDTO.of(role);
    }

    private AccountRole getRoleOrThrow(long id){

        return roleRepository.find("id = ?1",id)
                                                .firstResultOptional()
                                                .orElseThrow(() -> new EntityNotFoundException("Role with this id: "+ id + " does not exist"));
    }

    public AccountRole getRoleOrThrow(String roleName){

        return roleRepository.find("roleName = ?1",roleName)
                .firstResultOptional()
                .orElseThrow(() -> new EntityNotFoundException("Role with this name: "+ roleName + " does not exist"));
    }


    private void checkRole(String roleName){

        roleRepository.find("roleName = ?1",roleName)
                                   .firstResultOptional()
                                   .ifPresent(
                role -> {
                    throw new EntityExistsException("Role with this name: "+ roleName + " already exist");
                });
    }

    private void checkCount(long id){
        long accountWithRoleCount =  accountService.countRoleByRoleId(id);
        if(accountWithRoleCount != 0) throw new RoleDeleteException("Cannot delete role because it is associated with " + accountWithRoleCount + " accounts.");
    }


}
