package kakaopay.sprinkle.repository;

import kakaopay.sprinkle.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
