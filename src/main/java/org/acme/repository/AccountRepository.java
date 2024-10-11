package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.Account;

@ApplicationScoped
public class AccountRepository implements PanacheRepository<Account> {
}
