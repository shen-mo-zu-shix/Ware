package org.example.ware.exception;

import lombok.*;


@Builder
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class xException extends RuntimeException{
    private Code code;
    private int number;
    private String message;

}
