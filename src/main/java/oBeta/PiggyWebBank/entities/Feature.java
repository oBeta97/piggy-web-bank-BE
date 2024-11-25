package oBeta.PiggyWebBank.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "features")
@Data
@NoArgsConstructor
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false, unique = true)
    private String  name;

    @JsonIgnore
    @ManyToMany(mappedBy = "featureList")
    private List<Role> roleList;

    public Feature(String name) {
        this.name = name;
    }
}