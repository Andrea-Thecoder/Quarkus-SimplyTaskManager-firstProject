package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.AccountRole;

@ApplicationScoped
public class AccountRoleRepository implements PanacheRepository<AccountRole> {

}
