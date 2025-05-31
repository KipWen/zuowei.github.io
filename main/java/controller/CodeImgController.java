package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import util.DrawCheckCode;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class CodeImgController {

    @RequestMapping("/getCodeImg")
    public void drawCode(HttpServletRequest request, HttpServletResponse response) throws IOException {

        DrawCheckCode drawCheckCode = new DrawCheckCode();
        BufferedImage image = drawCheckCode.doDraw();

        HttpSession session = request.getSession();
        session.setAttribute("checkCode", drawCheckCode.getCheckCode());

        //这只响应头，防止缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpeg", out);
        out.flush();
        out.close();
    }

}
