package org.acme.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"title", "account_id"})
})
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Setter @Getter
public class Task {

    @Id //indica che una pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //indica che deve essere generato autoamticamente dal DB.
    @Column(nullable = false, columnDefinition = "bigint ")
    private Long id;

    @Column(nullable = false, length = 40)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @ManyToOne //(fetch = FetchType.LAZY)  // Fetch LAZY per evitare il caricamento dell'intero oggetto Account
    @JoinColumn(nullable = false)
    private Account account;

    @Column(name="create_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createAt;

    @Column(name = "update_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updateAt;

    //mysql non accetta boolean type in nessuna forma, per questo si usa coem convenzione il TININT(1), esso consuam 1byte, dove 0 è false, altro valore è true. (1) è puramente per convenzione per far capire ai programmatori che stiamo parlando di una colonna boolean. Tuttavia Postgres li supporta||
    @Column(name="is_complete", nullable = false, columnDefinition = "boolean default false")
    private boolean isComplete;

    //note sul costruttore: quando JPA fa una request al server , esempio un findbyid, se ilrecord viene trovato creerà un istanza dell'oggetto. Questo processo farà triggerare ò'attivazione del costruttore che setterà dei campi di default (se l'abbiamo impostato), questa cosa non impedirà/genererà errore in quanto il costruttore verrà attivato prima del trigger dei setter che setteranno i campi dell'oggetto creato con i valori presi dal db.
    public Task() {
        this.isComplete = false;
    }

    @PrePersist
    public void prePersist () {
        this.createAt = LocalDateTime.now();  // Imposta solo al momento dell'inserimento
        this.updateAt = LocalDateTime.now();  // Anche il primo aggiornamento

    }

    //annotazione con method associato necessario per aggioranre i valore di update at quando avviene un .persist su un entità gia esistente ovvero quando si aggiorna un entity.
    @PreUpdate
    public void onUpdate(){
        this.updateAt = LocalDateTime.now();
    }
}
