package org.acme.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
//in caso di ridodanza ciclica dei dati (esempio accoutn chiama task , task chiama account inizia la ridodanza ciclica) ,possiamo usare questa annotazione per dire che nel ciclo si limiti a mostrare la proprietà id
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Getter @Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "bigint ")
    private long id;

    @Column(nullable=false, columnDefinition = "varchar(30)")
    private String username;

    @Column(nullable=false, columnDefinition = "varchar(255)")
    private String password;

    @Column(nullable=false, unique=true, columnDefinition = "varchar(60)")
    private String email;


    //relazione N:1 ovvero  l'account può avre 1 solo ruolo ma  il ruolo può essere scelto da più account.
    @ManyToOne
    @JoinColumn(name = "role_id",nullable = false,columnDefinition = "bigint")
    private AccountRole role;

    //relazione 1:N. nel mappedBy ci emttiamo  l'entità che sta mappando ,q uindi user
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    @OneToOne(mappedBy = "accountId", cascade = CascadeType.ALL)
    //@JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(name="create_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createAt;

    @Column(name = "update_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updateAt;

    //si flagga a true quando si ordina un DELETE dell'user, in questo modo effettuaiamo una cancellazione logica.
    @Column(name="is_deleted", nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted;

    @Column(name = "delete_at", columnDefinition = "TIMESTAMP DEFAULT NULL")
    private LocalDateTime deleteAt;

    public Account(){
        this.isDeleted=false;
    }

    @PrePersist
    public void onPersist () {
        this.createAt = LocalDateTime.now();  // Imposta solo al momento dell'inserimento
        this.updateAt = LocalDateTime.now();  // Anche il primo aggiornamento
    }

    @PreUpdate
    public void onUpdate(){
        this.updateAt = LocalDateTime.now();
    }

}
