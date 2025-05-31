package util;

import lombok.Data;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

@Data
public class DrawCheckCode {

    //验证码
    public String checkCode;

    public final int checkCodeNum = 4;  //验证码包含checkCodeNum个数字

    private static final String CHAR_STR = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    //获取一个随机色
    public Color getColor(){
        Random random = new Random();
        int r = random.nextInt(256);  //[0,256)
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return new Color(r, g, b);
    }

    //随机生成一个验证码的值 4位数的整数
//    public String getNum(){
//        Random random = new Random();
//        String value = "";
//        for (int i = 0; i < checkCodeNum; i++) {
//            int rand = random.nextInt( 10);  //[0,10)
//            value = value + String.valueOf(rand);
//        }
//        return value;
//    }

    // 生成随机验证码（数字+字母）
    public String getNum(){
        Random random = new Random();
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < checkCodeNum; i++) {
            int index = random.nextInt(CHAR_STR.length());
            value.append(CHAR_STR.charAt(index));
        }
        return value.toString();
    }

    public BufferedImage doDraw(){
        //开始绘制验证码
        //1、先生成一个图片（需要在图片上画），设置图片的长宽以及图片类型
        BufferedImage image = new BufferedImage(100, 30, BufferedImage.TYPE_INT_RGB);

        //2、从图片上获取相应画笔，在图片上画画
        Graphics g = image.getGraphics();

        //3、画画
        //（1）画出一个长方形
        g.fillRect(0, 0,  100, 30);

        //画干扰线
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            int xBegin = random.nextInt(110);
            int yBegin = random.nextInt(30);
            int xEnd = random.nextInt(xBegin + 200);
            int yEnd = random.nextInt( yBegin + 10);
            g.setColor(getColor());
            //开始绘制线条
            g.drawLine(xBegin, yBegin, xEnd, yEnd);
        }

        //画随机数
        String num = getNum();
        this.checkCode = num;
        g.setColor(Color.BLACK);
        g.setFont(new Font("serif", Font.BOLD, 20));

        for (int i = 0; i < num.length(); i++) {
            g.drawString(String.valueOf(num.charAt(i)), 8 + 25 * i, 13 + random.nextInt(18));
        }
        return image;
    }

    public static void main(String[] args) throws IOException {
        DrawCheckCode drawCheckCode = new DrawCheckCode();
        BufferedImage bufferedImage = drawCheckCode.doDraw();

        File outputfile = new File("/Users/mac/Desktop/saved_image.png");
        ImageIO.write(bufferedImage, "png", outputfile);
        System.out.println("图像已保存!");
    }





}
