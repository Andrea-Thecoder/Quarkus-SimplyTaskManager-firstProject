package org.acme.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity()
@Table(name="roles")
@Getter @Setter
public class AccountRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "bigint ")
    private long id;

    @Column(name = "role_name",unique = true,nullable = false,columnDefinition = "varchar(20)")
    private String roleName;

    public AccountRole(){};

}
