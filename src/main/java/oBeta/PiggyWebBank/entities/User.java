package oBeta.PiggyWebBank.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oBeta.PiggyWebBank.exceptions.NotAllowedException;
import oBeta.PiggyWebBank.payloads.UserDTO;

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
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;


    public User(String surname, String username, String email, String password, LocalDate lastPasswordUpdate, Role role, String name) {
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.lastPasswordUpdate = lastPasswordUpdate;
        this.role = role;
        this.name = name;
    }

    public User(UserDTO dto, Role role){
        this.email = dto.email();
        this.username = dto.username();
        this.password = dto.password();
        this.lastPasswordUpdate = dto.lastPasswordUpdate();
        this.name = dto.name();
        this.surname = dto.surname();
        this.role = role;
    }

    public void setNewPassword(String _password){

        // avoid to set the same password when updated
        if(_password.equals(this.getPassword())) throw new NotAllowedException("The password must be different from the existing one");

        this.password = _password;
        this.lastPasswordUpdate = LocalDate.now();
    }


}