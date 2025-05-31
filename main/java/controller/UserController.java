package controller;

import dto.ResponseDTO;
import dto.UserLoginRequestDTO;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * RestController 是一个组合注解，它结合了@Controller和@ResponseBody的功能。
 * 当一个类被@RestController注解时，它的每个方法都会自动将返回的对象序列化为JSON或XML格式，
 * 并将其直接写入HTTP响应体中，而不是跳转到一个视图页面。
 * 这使得@RestController非常适合创建RESTful风格的API，因为它简化了返回数据的过程。
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseDTO<String> login(HttpServletRequest request, @RequestBody UserLoginRequestDTO requestDTO){
        ResponseDTO<String> responseDTO = new ResponseDTO<String>();
        System.out.println(requestDTO.toString());

        HttpSession session = request.getSession();
        String checkCode = (String)session.getAttribute("checkCode");

        if (checkCode != null && !checkCode.equalsIgnoreCase(requestDTO.getCheckCode())) {
            responseDTO.setMsg("验证码错误");
            responseDTO.setData("error");
        }else{
            responseDTO.setCode("200");
            responseDTO.setMsg("登入成功");
            responseDTO.setData("success");
        }

        User user = userService.findUserById(1);
        System.out.println(user.toString());

        return responseDTO;
    }

}
