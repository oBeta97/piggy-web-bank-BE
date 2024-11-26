package oBeta.PiggyWebBank.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oBeta.PiggyWebBank.exceptions.NotAllowedException;
import oBeta.PiggyWebBank.payloads.signin.SigninDTO;
import oBeta.PiggyWebBank.payloads.UserDTO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User implements UserDetails {

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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;


    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactionList;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserCharacteristic userCharacteristic;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MonthHistory> monthHistoryList;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Goal> goalList;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TransactionCategory> transactionCategoryList;


    public User(String surname, String username, String email, String password, LocalDate lastPasswordUpdate, Role role, String name) {
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.lastPasswordUpdate = lastPasswordUpdate;
        this.role = role;
        this.name = name;
    }

    public User(UserDTO dto, String password, Role role){
        this.email = dto.email();
        this.username = dto.username();
        this.password = password;
        this.lastPasswordUpdate = LocalDate.now();
        this.name = dto.name();
        this.surname = dto.surname();
        this.role = role;
    }

    public User(SigninDTO dto, String password, Role role){
        this.email = dto.email();
        this.username = dto.username();
        this.password = password;
        this.lastPasswordUpdate = LocalDate.now();
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role.
                getFeatureList().
                stream().
                map(feature ->
                        new SimpleGrantedAuthority(feature.getName())
                ).
                collect(Collectors.toList());
    }


}