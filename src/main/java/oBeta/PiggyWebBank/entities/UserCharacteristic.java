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
    private long id;

    @Column(nullable = false)
    private String avatar;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false, name = "daily_amount")
    private Double dailyAmount;

    @Column(nullable = false, name = "today_amount")
    private Double todayAmount;

    @Column(nullable = false, name = "minimum_savings")
    private Double minimumSavings;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    public UserCharacteristic(String avatar, String currency, Double dailyAmount, Double todayAmount, Double minimumSavings, User user) {
        this.avatar = avatar;
        this.currency = currency;
        this.dailyAmount = dailyAmount;
        this.todayAmount = todayAmount;
        this.minimumSavings = minimumSavings;
        this.user = user;
    }

    // Constructor for the new UserCharacteristic made by SignIn!
    public UserCharacteristic(String currency, User user){
        setDefaultAvatar(user);
        this.currency = currency;
        this.dailyAmount = 0.0;
        this.todayAmount = 0.0;
        this.minimumSavings = 0.0;
        this.user = user;
    }


    private void setDefaultAvatar(User user) {
        this.avatar = "https://ui-avatars.com/api/?name=" + user.getName() + "+" + user.getSurname();
    }
}
