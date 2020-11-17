package kakaopay.sprinkle.service;

import kakaopay.sprinkle.common.util.TokenUtil;
import kakaopay.sprinkle.dto.SprinkleRequest;
import kakaopay.sprinkle.entity.Sprinkle;
import kakaopay.sprinkle.entity.Token;
import kakaopay.sprinkle.repository.SprinkleRepository;
import kakaopay.sprinkle.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 뿌리기
 *
 * @author najisu
 * @version 2.0
 * @since 2020.11.17
 */
@Service
@RequiredArgsConstructor
public class SprinkleService {

    private final TokenService tokenService;

    private final SprinkleRepository sprinkleRepository;
    private final TokenRepository tokenRepository;

    public String setSprinkle(long userId, long roomId, SprinkleRequest sprinkleRequest) {

        long tokenId = tokenService.registerToken();



        return "";
    }

    public String receive(long userId, long roomId, String token) {

        return "";
    }

    public Sprinkle check(long roomId, String token) {

        return sprinkleRepository.findByRoomId(roomId);
    }
}
