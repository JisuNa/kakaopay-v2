package kakaopay.sprinkle.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ROOM_JOIN")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoomJoin extends BaseEntity {

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

}
