package org.acme.task.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.task.model.Task; // Assicurati che il modello Task sia importato correttamente

import java.util.List;
import java.util.Optional;

@ApplicationScoped //è un bean! questo tipo ha unciclo di vita  pari alla durata dell'applicazione. Tutti i componenti posson oaccedere a questo bean. la gestione del bean è sottocontrollo diQuarkus quindi la crea e distrugge lui!
public class TaskRepository implements PanacheRepository<Task>{

    public Optional<Task> findByTitle(String title) {
            return find("title", title).firstResultOptional();
    }

    //creazione di un method per far una query personalizzata:
    public List<Task> findByCompletion(boolean isComplete){
        // il method nativo di panache find accetta una JPQL(java persistance query language ) o HQL (hibernate query languge) ovvero  una stringa di query. Come secondo aprams accetta un oggetto ove siano presenti i vari aprametri di ricerca della query.
        //Bisognau sare dei segna posto dentro la query e per farlo usiamo il classico ? seguito dal valore del params di riferimento es. ?1 = si sta riferendo al primo params che incontra nell'oggetto dei parametri.
        return find("isComplete = ?1", isComplete).list();
    }
}
