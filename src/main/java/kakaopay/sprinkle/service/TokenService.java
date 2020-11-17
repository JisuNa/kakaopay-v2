package kakaopay.sprinkle.service;

import kakaopay.sprinkle.common.util.TokenUtil;
import kakaopay.sprinkle.entity.Token;
import kakaopay.sprinkle.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public long registerToken() {
        Token token = tokenRepository.save(
                Token.builder().token(TokenUtil.generate()).build()
        );

        return token.getId();
    }

}
