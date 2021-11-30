package ru.project.photoblog.mail;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.project.photoblog.mail.MailConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;

@RestController
@RequestMapping("api/email")
@CrossOrigin
public class MailController {

    @Autowired
    public JavaMailSender emailSender;

    @ResponseBody
    @RequestMapping("/sendSimpleEmail")
    public String sendSimpleEmail(@RequestParam("param") String param) {

        // создать SimpleMailMessage
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(MailConstants.DEFAULT_RECEIVER_EMAIL);
        message.setSubject("Уведомление о регистрации на сайте PhotoBlog");
        message.setText("Добрый день. Регистрация прошла успешно.");
        message.setText("param = " + param);
        // отправить
        this.emailSender.send(message);

        return "Письмо отправлено! с параметром: " + param;
    }

}