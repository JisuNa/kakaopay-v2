package kakaopay.sprinkle.common.error.exception;

public class EmptyInfoException extends RuntimeException {

    public EmptyInfoException() {
        super("해당 정보가 존재하지 않습니다.");
    }

}
