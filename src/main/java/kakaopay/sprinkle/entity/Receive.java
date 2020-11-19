package kakaopay.sprinkle.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "RECEIVE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Receive extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "sprinkle_id", nullable = false)
    private Long sprinkleId;

    @Builder
    public Receive(int amount, Long sprinkleId) {
        this.amount = amount;
        this.sprinkleId = sprinkleId;
    }

//    @ManyToOne
//    @JoinColumn

}
