package kakaopay.sprinkle.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "SPRINKLE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SprinkleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(name = "room_seq")
    public int roomSeq;

}
