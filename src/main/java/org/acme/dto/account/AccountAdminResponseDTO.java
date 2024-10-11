package org.acme.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.acme.model.Account;
import org.acme.model.Profile;

import java.time.LocalDateTime;

@Setter
@Getter
public class AccountAdminResponseDTO extends  AccountResponseDTO{

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @JsonProperty("isDeleted")
    private boolean isDeleted;

    private LocalDateTime deleteAt;

    private Profile profile;


    public static AccountAdminResponseDTO of (Account account){
        AccountAdminResponseDTO dto = new AccountAdminResponseDTO();
        //Creazione dell'of base. Non è strettamente necesario usare questa logica tuttavia cosi facendo offriamo modularità e riutilizzo del codice. Infatti se la classe base cambiasse la logica del 'of noi avremmo di riflesso le modifiche già fatte anche qua.
        AccountResponseDTO baseDto = AccountResponseDTO.of(account);
        dto.setId(baseDto.getId());
        dto.setUsername(baseDto.getUsername());
        dto.setEmail(baseDto.getEmail());
        dto.setRoleName(baseDto.getRoleName());
        dto.setTasks(baseDto.getTasks());


        dto.setCreateAt(account.getCreateAt());
        dto.setUpdateAt(account.getUpdateAt());
        dto.setDeleted(account.isDeleted());
        dto.setDeleteAt(account.getDeleteAt());
        dto.setProfile(account.getProfile());

        return dto;
    }
}
