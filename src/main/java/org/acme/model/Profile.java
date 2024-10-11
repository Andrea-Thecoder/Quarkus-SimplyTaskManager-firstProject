package org.acme.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "profiles")
@Setter @Getter
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "bigint ")
    private long id;

    @OneToOne()
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account accountId;

    @Column(nullable = false,columnDefinition = "varchar(80)")
    private String firstname;

    @Column(nullable = false,columnDefinition = "varchar(80)")
    private String lastname;

    @Column(nullable = false,columnDefinition = "varchar(35)")
    private String country;

    @Column(name = "phone_number", nullable = false,columnDefinition = "varchar(15)")
    private String phoneNumber;

    @Column(name = "tax_code", nullable = false,columnDefinition = "Char(16)")
    private String taxCode;

    @Column(nullable = false,columnDefinition = "DATE")
    private LocalDate birthDate;

    @Column(name = "update_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updateAt;

    @PrePersist
    private void onPersist() {
        updateAt = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdate() {
        updateAt = LocalDateTime.now();
    }
}
