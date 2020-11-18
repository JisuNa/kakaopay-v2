package kakaopay.sprinkle.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SPRINKLE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Sprinkle extends BaseEntity {

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "number_of_recipients", nullable = false)
    private int numberOfRecipients;

    @Column(name = "status")
    private String status;

    @Column(name = "sprayed_amount")
    private int sprayedAmount;

    @Column(name = "token", nullable = false)
    private String token;

    @Builder
    public Sprinkle(Long roomId, Long userId, int amount, int numberOfRecipients, String token) {
        this.roomId = roomId;
        this.userId = userId;
        this.amount = amount;
        this.numberOfRecipients = numberOfRecipients;
        this.token = token;
    }

//    @OneToOne(mappedBy = "token")
//    private Token token;
//
//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "skprinkle")
//    private List<Receive> receiveList = new ArrayList<>();

}
