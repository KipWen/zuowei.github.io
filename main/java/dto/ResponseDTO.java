package dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseDTO<T> implements Serializable {
    private String code;
    private String msg;
    private T data;


}
