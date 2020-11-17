package kakaopay.sprinkle.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "RECEIVE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Receive extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "sprinkle_id", nullable = false)
    private Long sprinkleId;

//    @ManyToOne
//    @JoinColumn

}
