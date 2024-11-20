package oBeta.PiggyWebBank.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "user_characteristics")
@Data
@NoArgsConstructor
public class UserCharacteristic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String avatar;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false, name = "daily_amount")
    private float dailyAmount;

    @Column(nullable = false, name = "today_amount")
    private float todayAmount;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    public UserCharacteristic(String avatar, String currency, float dailyAmount, float todayAmount, User user) {
        this.avatar = avatar;
        this.currency = currency;
        this.dailyAmount = dailyAmount;
        this.todayAmount = todayAmount;
        this.user = user;
    }
}
