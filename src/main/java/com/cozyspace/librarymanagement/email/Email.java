package com.cozyspace.librarymanagement.email;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.user.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class Email {
    private static Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", MailConfig.HOST_NAME);
        props.put("mail.smtp.socketFactory.port", MailConfig.SSL_PORT);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.port", MailConfig.SSL_PORT);

        return Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
            }
        });
    }

    private static Integer generateCode() {
        Random random = new Random();
        return random.nextInt(100000, 1000000);
    }

    public static void sendEmailValidationCode(String receiveEmail) {
        Integer code = generateCode();
        DataTransfer.getInstance().getDataMap().put("emailValidationCode", code.toString());

        Session session = createSession();

        try {
            MimeMessage message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiveEmail));
            message.setSubject("Mã xác thực để tạo tài khoản truy cập thư viện");
            message.setText("Mã xác thực của bạn là: %d".formatted(code));

            Transport.send(message);

        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void sendResetPasswordConfirmationEmail(String receiveEmail) {
        Integer code = generateCode();
        DataTransfer.getInstance().getDataMap().put("resetPasswordCode", code.toString());

        Session session = createSession();

        try {
            MimeMessage message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiveEmail));
            message.setSubject("Mã xác thực để khôi phục tài khoản");
            message.setText("""
                    Mã xác minh dùng để khôi phục tài khoản %s là: %d.
                    Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.
                    """.formatted(DataTransfer.getInstance().getDataMap().get("loginUsername"), code));
            Transport.send(message);

        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }
    }
}
