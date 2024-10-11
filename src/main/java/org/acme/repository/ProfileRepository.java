package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.Profile;

@ApplicationScoped
public class ProfileRepository implements PanacheRepository<Profile> {
}
