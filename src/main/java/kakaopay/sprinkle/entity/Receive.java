package kakaopay.sprinkle.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "RECEIVE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Receive extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receive_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "sprinkle_id", nullable = false)
    private Long sprinkleId;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sprinkle_id", insertable = false, updatable = false)
    private Sprinkle sprinkle;

    @Builder
    public Receive(BigDecimal amount, Long sprinkleId) {
        this.amount = amount;
        this.sprinkleId = sprinkleId;
    }

    public void updateUserId(Long userId) {
        this.userId = userId;
    }

}
