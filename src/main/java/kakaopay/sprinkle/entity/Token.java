package kakaopay.sprinkle.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "TOKEN")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Token extends BaseEntity {

    @Column(name = "token", nullable = false)
    private String token;

//    @OneToOne
//    @JoinColumn(name = "token_id")
//    private Sprinkle sprinkle;

    @Builder
    public Token(String token) {
        this.token = token;
    }

}
