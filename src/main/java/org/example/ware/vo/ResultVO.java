package org.example.ware.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ware.exception.Code;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultVO {
    private int code;
    private String message;
    private Object data;
    //////////////////////////////////////////////////////////////
    public static final ResultVO EMPTY = ResultVO.builder()
            .code(200)
            .build();

    public static ResultVO ok() {
        return EMPTY;
    }

    //////////////////////////////////////////////////////////////
    public static ResultVO success(Object data) {
        return ResultVO.builder()
                .code(200)
                .data(data)
                .build();
    }

    //////////////////////////////////////////////////////////////
    public static ResultVO error(Code code) {
        return ResultVO.builder()
                .code(code.getCode())
                .message(code.getMessage())
                .build();
    }

    //////////////////////////////////////////////////////////////
    public static ResultVO error(int code, String message) {
        return ResultVO.builder()
                .code(code)
                .message(message)
                .build();
    }
}
