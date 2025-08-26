package part2.common.enums;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    SUCCESS(0,"success"),
    FAIL(1,"fail"),
    ;
    private int code;
    private String msg;

    ExceptionCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
