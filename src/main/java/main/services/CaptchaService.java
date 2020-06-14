package main.services;

import lombok.Getter;
import main.API.ResponseApi;
import main.repositories.CaptchaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class CaptchaService {
    private final CaptchaRepository captchaRepository;

    @Autowired
    public CaptchaService(CaptchaRepository captchaRepository) {
        this.captchaRepository = captchaRepository;
    }

    private String generateCaptchaTextMethod(int captchaLength)   {
        String saltChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuffer captchaStrBuffer = new StringBuffer();
        java.util.Random rnd = new java.util.Random();
        while (captchaStrBuffer.length() < captchaLength) {
            int index = (int) (rnd.nextFloat() * saltChars.length());
            captchaStrBuffer.append(saltChars, index, index + 1);
        }
        return captchaStrBuffer.toString();
    }
    @Transactional
    public ResponseApi captchaGen(HttpServletRequest request){
        String FILE_TYPE = "jpeg";
        String captchaSecret = null;
        String bosToB64 = null;
        String captchaStr = null;

        try {
            captchaStr = generateCaptchaTextMethod(6);
            captchaSecret = generateCaptchaTextMethod(16);

            int width = 100;
            int height = 35;

            Color bg = new Color(0,255,255);
            Color fg = new Color(0,100,0);

            Font font = new Font("Arial", Font.BOLD, 20);
            BufferedImage cpimg = new BufferedImage(width,height, BufferedImage.OPAQUE);
            Graphics g = cpimg.createGraphics();

            g.setFont(font);
            g.setColor(bg);
            g.fillRect(0, 0, width, height);
            g.setColor(fg);
            g.drawString(captchaStr,10,25);

            HttpSession session = request.getSession(true);
            session.setAttribute("CAPTCHA", captchaStr);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(cpimg, FILE_TYPE, bos);

            bosToB64 = Base64.getEncoder().encodeToString(bos.toByteArray());
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        captchaRepository.updateCodes(captchaStr, captchaSecret);
        captchaRepository.deleteOlderThan60Minutes();
        return ResponseApi.builder()
                .secret(captchaSecret).image("data:image/jpeg;base64, " + bosToB64).build();
    }
}
