package kakaopay.sprinkle.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Code {

    SUCCESS("0000", "정상처리"),
    ERROR0001("0001", "필수값 누락"),
    ERROR0002("0002", "중복"),
    ERROR0003("0003", "올바르지 않은 정보");

    private final String code;
    private final String message;

}
