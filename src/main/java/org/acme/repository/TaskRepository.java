package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.Task;

@ApplicationScoped //è un bean! questo tipo ha unciclo di vita  pari alla durata dell'applicazione. Tutti i componenti posson oaccedere a questo bean. la gestione del bean è sottocontrollo diQuarkus quindi la crea e distrugge lui!
public class TaskRepository implements PanacheRepository<Task> {

}
