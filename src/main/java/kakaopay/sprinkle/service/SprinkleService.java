package kakaopay.sprinkle.service;

import kakaopay.sprinkle.dto.SprinkleRequest;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class SprinkleService {

    //

    public String sprinkle(BigInteger userId, BigInteger roomId, SprinkleRequest sprinkleRequest) {

        return "";
    }

    public String receive(BigInteger userId, BigInteger roomId, String token) {

        return "";
    }

    public String check(BigInteger roomId, String token) {

        return "";
    }
}
