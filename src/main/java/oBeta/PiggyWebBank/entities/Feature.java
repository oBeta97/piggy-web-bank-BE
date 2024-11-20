package oBeta.PiggyWebBank.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "features")
@Data
@NoArgsConstructor
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String  name;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public Feature(String name, Role role) {
        this.name = name;
        this.role = role;
    }
}