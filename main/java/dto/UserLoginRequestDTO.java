package dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequestDTO implements Serializable {

    private String username;    //用户名

    private String password;    //密码

    private String checkCode;    //验证码

}
