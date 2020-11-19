package kakaopay.sprinkle.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SPRINKLE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Sprinkle extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sprinkle_id")
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "number_of_recipients", nullable = false)
    private int numberOfRecipients;

    @Column(name = "status")
    @ColumnDefault("PROCESSING")
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

    public void updateStatus(String status) {
        this.status = status;
    }

    @OneToMany(mappedBy = "sprinkle")
    @OrderBy("id desc")
    private List<Receive> receiveList = new ArrayList<>();

}
