package oBeta.PiggyWebBank.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oBeta.PiggyWebBank.exceptions.NotAllowedException;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String name;
    private String surname;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private String password;

    @Column(name = "last_password_update", nullable = false)
    @Setter(AccessLevel.NONE)
    private LocalDate lastPasswordUpdate;


    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private Role role;


    public User(String email, String username, LocalDate lastPasswordUpdate, String password, String name, String surname) {
        this.email = email;
        this.username = username;
        this.lastPasswordUpdate = lastPasswordUpdate;
        setPassword(password);
        this.name = name;
        this.surname = surname;
    }

    public void setPassword(String _password){

        // avoid to set the same password when updated
        if(_password.equals(this.getPassword())) throw new NotAllowedException("The password must be different from the existing one");

        this.password = _password;
        this.lastPasswordUpdate = LocalDate.now();
    }


}